package cn.hutool.core.statemachine;

import java.util.*;

/**
 * 状态机，用来定义状态的起始以及流转规则
 * 状态机只负责定义状态的流转,状态流转的条件由业务处理,符合条件才发送某个事件
 * @author 肖海斌
 * @param <S> 状态类型
 * @param <E> 事件类型
 */
public class StateMachine<S,E> {
	/**
	 * 是否支持状态自旋，如a1状态发生e事件，状态变为a2，在a2状态下如果再发生e事件，状态仍为a2
	 * 默认支持自旋
	 */
	private boolean spinSupport = true;
	/**
	 * 当前状态对应的规则集
	 */
	private Map<S, List<Rule>> ruleMap = new HashMap<>();
	/**
	 * 目标状态对应的事件集
	 */
	private Map<S,Set<E>> targetStatusEventMap = new HashMap<>();
	/**
	 * 起始状态集合
	 */
	private Set<S> startStatusSet = new HashSet<>();
	/**
	 * 目标状态集合
	 */
	private Set<S> targetStatusSet = new HashSet<>();
	/**
	 * 状态机本身,用来实现初始化状态机时进行链式操作
	 */
	private final StateMachine<S,E> innerStateMachine = this;

	public StateMachine(){

	}
	public StateMachine(boolean spinSupport){
		this.spinSupport = spinSupport;
	}
	/**
	 * 设置状态机的起始状态集
	 * @param status
	 */
	@SuppressWarnings("unchecked")
	public StateMachine<S,E> setStartStatus(S ...status) {
		if(status !=null && status.length > 0) {
			for(S s : status) {
				startStatusSet.add(s);
			}
		}
		return this;
	}

	/**
	 * 链式操作增加规则时，定义当前状态
	 * @param status
	 * @return
	 */
	public CurrentStatus when(S status) {
		if(!startStatusSet.contains(status) && !targetStatusSet.contains(status)) {
			throw new RuntimeException("当前状态尚未定义");
		}
		return new CurrentStatus(status);
	}

	/**
	 * 添加一条规则
	 * @param statusFrom  当前状态
	 * @param statusTo	目标状态
	 * @param eventArr  当前状态迁移到目标状态时的事件列表
	 * @return
	 */
	public StateMachine<S,E> addRule(S statusFrom, S statusTo, E... eventArr){
		return when(statusFrom).occur(eventArr).then(statusTo);
	}

	/**
	 * 生成开始状态
	 * @return
	 */
	public S start(){
		if(startStatusSet.size()!=1) {
			throw new RuntimeException("存在多个起始状态，需要指定一个确定的起始状态");
		}
		return startStatusSet.iterator().next();
	}

	/**
	 * 有多个开始状态的情况下, 生成开始状态
	 * @param status
	 * @return
	 */
	public S start(S status) {
		if(!startStatusSet.contains(status)) {
			throw new RuntimeException(status + "不是起始状态");
		}
		return status;
	}

	/**
	 * 事件触发状态流转
	 * @param status
	 * @param event
	 * @return
	 */
	public S on(S status,E event) {
		List<Rule> ruleList = this.ruleMap.get(status);
		if(ruleList != null && ruleList.size()>0) {
			for(Rule rule : ruleList) {
				if(rule.isMatch(event)) {
					return rule.statusTo;
				} else {
					continue ;
				}
			}
		}
		//状态自旋处理
		if(spinSupport) {
			Set<E> eventSet = targetStatusEventMap.get(status);
			if (eventSet != null && eventSet.contains(event)) {
				return status;
			}
		}
		throw new RuntimeException("状态"+status+"与事件"+event+"不匹配");
	}

	/**
	 * 当前状态
	 */
	public class CurrentStatus {
		private S status;
		public CurrentStatus(S status) {
			this.status = status;
		}

		/**
		 * 发生某个事件
		 * @param event
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public StatusWaitJump occur(E... event) {
			return  new StatusWaitJump(status, event);
		}
	}

	/**
	 * 当前状态触发事件后待流转的中间状态
	 * occur
	 */
	public class StatusWaitJump {
		private E[] eventArr;
		private S statusFrom;
		StatusWaitJump(S statusFrom, E[] eventArr){
			this.statusFrom = statusFrom;
			this.eventArr = eventArr;
		}

		/**
		 * 定义状态跳转的目标状态
		 * @param statusTo
		 * @return
		 */
		public StateMachine<S,E> then(S statusTo) {
			Rule rule = new Rule(statusFrom, eventArr, statusTo);
			List<Rule> ruleList;
			if(ruleMap.containsKey(statusFrom)) {
				ruleList = ruleMap.get(statusFrom);
			} else {
				ruleList = new ArrayList<Rule>();
				ruleMap.put(rule.statusFrom, ruleList);
			}
			for(Rule ruleInter : ruleList) {
				for(E e: rule.eventSet) {
					//事件重复定义
					if (ruleInter.eventSet.contains(e)){
						throw new RuntimeException(statusFrom+"定义了重复的事件"+e);
					}
				}
			}
			ruleList.add(rule);
			targetStatusSet.add(statusTo);
			Set<E> eventSet;
			if(targetStatusEventMap.containsKey(rule.statusTo)) {
				eventSet = targetStatusEventMap.get(rule.statusTo);
			} else {
				eventSet = new HashSet<>();
				targetStatusEventMap.put(rule.statusTo, eventSet);
			}
			Collections.addAll(eventSet, eventArr);
			return innerStateMachine;
		}
	}

	/**
	 * 规则
	 */
	private class Rule {
		/**
		 * 事件集
		 */
		private Set<E> eventSet = new HashSet<>();
		/**
		 * 当前状态
		 */
		private S statusFrom;
		/**
		 * 目标状态
		 */
		private S statusTo;
		public Rule(S statusFrom, E[] eventArr, S statusTo) {
			for(E e : eventArr) {
				this.eventSet.add(e);
			}
			this.statusFrom=statusFrom;
			this.statusTo = statusTo;
		}

		/**
		 * 事件是否跟规则匹配
		 * @param event
		 * @return
		 */
		public boolean isMatch(E event) {
			if(eventSet.contains(event)) {
				return true;
			}
			return false;
		}
	}
}