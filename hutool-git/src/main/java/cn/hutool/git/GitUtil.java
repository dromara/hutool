package cn.hutool.git;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Git操作工具类
 *
 * @author 王斌
 */
public class GitUtil {
	private String fromGitUrl;
	private CredentialsProvider fromCp;
	private String toGitUrl;
	private CredentialsProvider toCp;
	private Git git;
	private File directory;

	private String remoteFrom = "origin";
	private String remoteTo = "origin";

	public static GitUtil from(String gitUrl, String username, String password) {
		GitUtil utils = fromUrl(gitUrl);
		utils.toCp(username, password);
		return utils;
	}

	public GitUtil to(String gitUrl, String username, String password) throws GitAPIException, URISyntaxException {
		toGitUrl(null, gitUrl);
		toCp(username, password);
		return this;
	}

	public static GitUtil fromUrl(String gitUrl) {
		GitUtil utils = new GitUtil();
		utils.setFromGitUrl(gitUrl);
		return utils;
	}

	public GitUtil toGitUrl(String remote, String gitUrl) throws GitAPIException, URISyntaxException {
		this.toGitUrl = gitUrl;
		RemoteAddCommand command = git.remoteAdd();
		command.setName(remote);
		this.remoteTo = remote;
		if (gitUrl.equals(this.fromGitUrl)) return this;
		command.setUri(new URIish(gitUrl));
		command.call();
		return this;
	}

	/**
	 * 配置from权限
	 *
	 * @param username 用户名
	 * @param password 密码
	 */
	public GitUtil fromCp(String username, String password) {
		this.fromCp = new UsernamePasswordCredentialsProvider(username, password);
		return this;
	}

	/**
	 * 检查推送结果是否成功
	 *
	 * @param call 推送结果
	 */
	public static void verifyOkPush(Iterable<PushResult> call) {
		boolean condition = StreamSupport.stream(call.spliterator(), false)
				.allMatch(p -> p.getRemoteUpdates().stream()
						.allMatch(u -> u.getStatus() == RemoteRefUpdate.Status.OK || u.getStatus() == RemoteRefUpdate.Status.UP_TO_DATE));
		if (!condition) {
			StreamSupport.stream(call.spliterator(), false)
					.map(pr -> pr.getRemoteUpdates().stream()
							.map(ru -> String.format("%s %s %s", ru.getStatus(), ru.getRemoteName(), ru.getMessage()))
							.collect(Collectors.joining(",")))
					.collect(Collectors.joining(","));
		}
	}

	/**
	 * 配置to权限
	 *
	 * @param username 用户名
	 * @param password 密码
	 */
	public GitUtil toCp(String username, String password) {
		this.toCp = new UsernamePasswordCredentialsProvider(username, password);
		return this;
	}

	/**
	 * 拉取git仓库
	 *
	 * @param localCloneLocation 拉取到本地哪个文件夹
	 */
	public GitUtil clone(String localCloneLocation) throws IOException, GitAPIException {
		String tempLocation = Files.createTempDirectory("temp-git-folder-").toString();
		CloneCommand command = Git.cloneRepository();
		command.setURI(fromGitUrl);
		if (ObjectUtil.isNotEmpty(fromCp)) {
			command.setCredentialsProvider(fromCp);
		}
		File tempFile = new File(tempLocation);
		command.setDirectory(tempFile);
		command.call();
		File localFile = new File(localCloneLocation);
		if (localFile.exists()) {
			FileUtil.del(localFile);
		}
		FileUtil.move(tempFile, localFile, true);
		this.directory = localFile;
		this.git = Git.open(this.directory);
		return this;
	}

	/**
	 * 提交本次修改
	 *
	 * @param commitComment 提交备注
	 */
	public GitUtil commit(String commitComment) throws GitAPIException {
		git.add().setUpdate(true).addFilepattern(".").call();
		git.add().addFilepattern(".").call();
		git.commit().setMessage(commitComment).call();
		return this;
	}

	public GitUtil push() throws GitAPIException, IOException {
		push(null);
		return this;
	}

	/**
	 * 推送代码
	 *
	 * @param branch 推送到哪个远程分支
	 */
	public GitUtil push(String branch) throws GitAPIException, IOException {
		push(null, branch, null);
		return this;
	}

	/**
	 * 推送代码
	 *
	 * @param remote 推送到哪个remote
	 * @param branch 推送到哪个远程分支
	 */
	public GitUtil push(String remote, String branch) throws GitAPIException, IOException {
		push(remote, branch, null);
		return this;
	}

	/**
	 * 推送代码
	 *
	 * @param branch  推送到哪个远程分支
	 * @param isForce 是否强制推送（会覆盖远程分支）
	 */
	public GitUtil push(String branch, boolean isForce) throws GitAPIException, IOException {
		push(null, branch, null, isForce);
		return this;
	}

	/**
	 * 推送代码
	 *
	 * @param remote  推送到哪个remote
	 * @param branch  推送到哪个远程分支
	 * @param isForce 是否强制推送（会覆盖远程分支）
	 */
	public GitUtil push(String remote, String branch, boolean isForce) throws GitAPIException, IOException {
		push(remote, branch, null, isForce);
		return this;
	}

	/**
	 * 推送代码
	 *
	 * @param branch 推送到哪个远程分支
	 * @param toCp   推送需要的权限
	 */
	public GitUtil push(String branch, CredentialsProvider toCp) throws GitAPIException, IOException {
		push(null, branch, toCp, false);
		return this;
	}

	/**
	 * 推送代码
	 *
	 * @param remote 推送到哪个remote
	 * @param branch 推送到哪个远程分支
	 * @param toCp   推送需要的权限
	 */
	public GitUtil push(String remote, String branch, CredentialsProvider toCp) throws GitAPIException, IOException {
		push(remote, branch, toCp, false);
		return this;
	}

	/**
	 * 推送代码
	 *
	 * @param branch  推送到哪个远程分支
	 * @param toCp    推送需要的权限
	 * @param isForce 是否强制推送（会覆盖远程分支）
	 */
	public GitUtil push(String branch, CredentialsProvider toCp, boolean isForce) throws GitAPIException, IOException {
		push(null, branch, toCp, isForce);
		return this;
	}

	/**
	 * 推送代码
	 *
	 * @param remote  推送到哪个remote
	 * @param branch  推送到哪个远程分支
	 * @param toCp    推送需要的权限
	 * @param isForce 是否强制推送（会覆盖远程分支）
	 */
	public GitUtil push(String remote, String branch, CredentialsProvider toCp, boolean isForce) throws GitAPIException, IOException {
		PushCommand command = git.push();
		if (ObjectUtil.isNotEmpty(toCp)) {
			command.setCredentialsProvider(toCp);
		} else if (this.toCp == null) {
			command.setCredentialsProvider(this.fromCp);
		} else {
			command.setCredentialsProvider(this.toCp);
		}

		if (ObjectUtil.isNotEmpty(remote)) {
			command.setRemote(remote);
		} else if (this.remoteTo == null) {
			command.setRemote(this.remoteFrom);
		} else {
			command.setRemote(this.remoteTo);
		}

		if (branch != null) {
			String currentBranch = git.getRepository().getBranch();
			if (!currentBranch.equals(branch)) {
				RefSpec refSpec = new RefSpec("refs/heads/" + currentBranch + ":" + branch);
				command.setRefSpecs(refSpec);
			}
			if (isForce) {
				command.setForce(true);
			}
		}

		Iterable<PushResult> results = command.call();
		verifyOkPush(results);
		return this;
	}

	/**
	 * 删除远程分支
	 *
	 * @param branch 要删除的远程分支名
	 */
	public GitUtil deleteRemoteBranch(String branch) throws GitAPIException {
		deleteRemoteBranch(null, branch);
		return this;
	}

	/**
	 * 删除远程分支
	 *
	 * @param remote 要删除哪个remote
	 * @param branch 要删除的远程分支名
	 */
	public GitUtil deleteRemoteBranch(String remote, String branch) throws GitAPIException {
		deleteRemoteBranchWithCp(remote, branch, null);
		return this;
	}

	/**
	 * 删除远程分支
	 *
	 * @param branch 要删除的远程分支名
	 * @param toCp   删除所需的权限
	 */
	public GitUtil deleteRemoteBranchWithCp(String branch, CredentialsProvider toCp) throws GitAPIException {
		deleteRemoteBranchWithCp(null, branch, toCp);
		return this;
	}

	/**
	 * 删除远程分支
	 *
	 * @param remote 要删除哪个remote
	 * @param branch 要删除的远程分支名
	 * @param toCp   删除所需的权限
	 */
	public GitUtil deleteRemoteBranchWithCp(String remote, String branch, CredentialsProvider toCp) throws GitAPIException {
		//获取远程分支列表
		List<Ref> remoteBranchList = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
		String deleteBranchName = "refs/remotes/" + remote + "/" + branch;
		Optional<Ref> needBranch = remoteBranchList.stream().filter(ss -> deleteBranchName.equals(ss.getName())).findFirst();
		//判断有没有需要删除的分支
		if (needBranch.isPresent()) {
			git.branchDelete().setBranchNames(deleteBranchName).setForce(true).call();
			PushCommand command = git.push();
			RefSpec refSpec = new RefSpec().setSource(null).setDestination("refs/heads/" + branch);
			command.setRefSpecs(refSpec);
			if (ObjectUtil.isNotEmpty(toCp)) {
				command.setCredentialsProvider(toCp);
			} else if (this.toCp == null) {
				command.setCredentialsProvider(this.fromCp);
			} else {
				command.setCredentialsProvider(this.toCp);
			}
			if (ObjectUtil.isNotEmpty(remote)) {
				command.setRemote(remote);
			} else if (this.remoteTo == null) {
				command.setRemote(this.remoteFrom);
			} else {
				command.setRemote(this.remoteTo);
			}
			Iterable<PushResult> results = command.call();
			verifyOkPush(results);
		}
		return this;
	}

	/**
	 * 检出分支
	 * <p>
	 * 主要和 {@link GitUtil#checkout(String)} 区分开，代表拉取远程分支
	 *
	 * @param remote 需要检出的是哪个remote
	 * @param branch 分支名
	 * @see GitUtil#checkout(String)
	 */
	public GitUtil checkout(String remote, String branch) throws GitAPIException, IOException {
		//判断本地有没有要checkout的分支
		List<Ref> localBranchList = git.branchList().call();
		Optional<Ref> needBranch = localBranchList.stream().filter(ss -> ("refs/heads/" + branch).equals(ss.getName())).findFirst();
		if (needBranch.isPresent()) {
			//如果本地有需要checkout的分支
			String currentBranch = git.getRepository().getBranch();
			//判断当前分支是否是需要checkout的分支
			if (!currentBranch.equals(branch)) {
				//如果当前分支不是需要checkout的分支，则先checkout对应的本地分支
				git.checkout().setName(branch).call();
			}
			//将本地分支还原到远程分支的版本
			git.reset().setMode(ResetCommand.ResetType.HARD).setRef(remote + "/" + branch).call();
		} else {
			//如果本地没有对应分支则从远程拉取
			git.checkout().setName(branch)
					.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
					.setCreateBranch(true)
					.setStartPoint(remote + "/" + branch).call();
		}
		return this;
	}

	/**
	 * 检出分支
	 * <p>
	 * 本地有就会先检出本地的，没有则检出远程的，如果都没有则会从当前分支创建一个
	 *
	 * @param branch 分支名
	 */
	public GitUtil checkout(String branch) throws GitAPIException, IOException {
		//首先查看是否是当前分支，是的话就跳过
		if (git.getRepository().getBranch().equals(branch)) return this;
		//查看本地有没有需要的分支，如果有就checkout本地的
		List<Ref> branchList = git.branchList().call();
		Optional<Ref> needBranch = branchList.stream().filter(ss -> ("refs/heads/" + branch).equals(ss.getName())).findFirst();
		if (needBranch.isPresent()) {
			git.checkout().setName(needBranch.get().getName()).call();
			return this;
		}
		//如果本地没有就查看远程有没有，如果有就checkout远程的
		List<Ref> remoteBranchList = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
		needBranch = remoteBranchList.stream().filter(ss -> ("refs/remotes/origin/" + branch).equals(ss.getName())).findFirst();
		if (needBranch.isPresent()) {
			git.checkout().setName(branch)
					.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
					.setCreateBranch(true)
					.setStartPoint("origin/" + branch).call();
			return this;
		}
		//如果都没有就从当前分支创建一个
		git.checkout().setCreateBranch(true).setName(branch).call();
		return this;
	}

	/**
	 * 打标签
	 *
	 * @param tagName 标签名
	 */
	public GitUtil tag(String tagName) throws GitAPIException {
		git.tag().setName(tagName).call();
		return this;
	}

	/**
	 * 推送标签
	 */
	public GitUtil pushTags() throws GitAPIException {
		pushTags(null);
		return this;
	}

	/**
	 * 推送标签
	 *
	 * @param remote 推送到哪个remote
	 */
	public GitUtil pushTags(String remote) throws GitAPIException {
		PushCommand command = git.push().setPushTags();
		if (remote != null) {
			command.setRemote(remote);
		}
		command.call();
		return this;
	}

	public static GitUtil testing(Repository repository) {
		GitUtil utils = new GitUtil();
		utils.setGit(new Git(repository));
		return utils;
	}

	/*getter/setter start */
	public String getFromGitUrl() {
		return fromGitUrl;
	}

	public void setFromGitUrl(String fromGitUrl) {
		this.fromGitUrl = fromGitUrl;
	}

	public CredentialsProvider getFromCp() {
		return fromCp;
	}

	public void setFromCp(CredentialsProvider fromCp) {
		this.fromCp = fromCp;
	}

	public String getToGitUrl() {
		return toGitUrl;
	}

	public void setToGitUrl(String toGitUrl) {
		this.toGitUrl = toGitUrl;
	}

	public CredentialsProvider getToCp() {
		return toCp;
	}

	public void setToCp(CredentialsProvider toCp) {
		this.toCp = toCp;
	}

	public Git getGit() {
		return git;
	}

	public void setGit(Git git) {
		this.git = git;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public String getRemoteFrom() {
		return remoteFrom;
	}

	public void setRemoteFrom(String remoteFrom) {
		this.remoteFrom = remoteFrom;
	}

	public String getRemoteTo() {
		return remoteTo;
	}

	public void setRemoteTo(String remoteTo) {
		this.remoteTo = remoteTo;
	}

	/*getter/setter end*/
}
