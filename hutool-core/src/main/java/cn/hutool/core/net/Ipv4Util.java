package cn.hutool.core.net;

import cn.hutool.core.util.StrUtil;

import java.util.*;

/**
 * IP地址工具类
 *
 * @author ZhuKun
 * @date 2020-08-21
 */
public class Ipv4Util {
    /**
     * IP段的分割符
     */
    public static final String IP_SPLIT_MARK = "-";

    /**
     * IP与掩码的分割符
     */
    public static final String IP_MASK_SPLIT_MARK = "/";

    /**
     * 最大掩码位
     */
    public static final int IP_MASK_MAX = 32;

    /**
     * 格式化IP段
     *
     * @param ip   IP地址
     * @param mask 掩码
     * @return 返回xxx.xxx.xxx.xxx/mask的格式
     */
    public static String formatIpBlock(String ip, String mask) {
        return ip + IP_MASK_SPLIT_MARK + getMaskBitByMask(mask);
    }

    /**
     * 智能转换IP地址集合
     *
     * @param ipRange IP段，支持X.X.X.X-X.X.X.X或X.X.X.X/X
     * @param isAll   true:全量地址，false:可用地址；仅在ipRange为X.X.X.X/X时才生效
     * @return IP集
     */
    public static List<String> list(String ipRange, boolean isAll) {
        if (ipRange.contains(IP_SPLIT_MARK)) {
            String[] range = ipRange.split(IP_SPLIT_MARK);
            return list(range[0], range[1]);
        } else if (ipRange.contains(IP_MASK_SPLIT_MARK)) {
            String[] param = ipRange.split(IP_MASK_SPLIT_MARK);
            return list(param[0], Integer.parseInt(param[1]), isAll);
        } else {
            List<String> ips = new ArrayList<>();
            ips.add(ipRange);
            return ips;
        }
    }

    /**
     * 根据IP地址、子网掩码获取IP地址区间
     *
     * @param ip      IP地址
     * @param maskBit 掩码位，例如24、32
     * @param isAll   true:全量地址，false:可用地址
     * @return
     */
    public static List<String> list(String ip, int maskBit, boolean isAll) {
        List<String> list = new ArrayList<>();
        if (maskBit == IP_MASK_MAX) {
            if (Boolean.TRUE.equals(isAll)) {
                list.add(ip);
            }
        } else {
            String startIp = getBeginIpStr(ip, maskBit);
            String endIp = getEndIpStr(ip, maskBit);
            String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
            String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
            if (Boolean.TRUE.equals(isAll)) {
                startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]));
                endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]));
            } else {
                startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
                endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
            }
            list = list(startIp, endIp);
        }
        return list;
    }

    /**
     * 得到IP地址区间
     *
     * @param ipFrom 开始IP
     * @param ipTo   结束IP
     * @return
     */
    public static List<String> list(String ipFrom, String ipTo) {
        List<String> ips = new ArrayList<>();
        String[] ipfromd = ipFrom.split("\\.");
        String[] iptod = ipTo.split("\\.");
        int[] int_ipf = new int[4];
        int[] int_ipt = new int[4];
        for (int i = 0; i < 4; i++) {
            int_ipf[i] = Integer.parseInt(ipfromd[i]);
            int_ipt[i] = Integer.parseInt(iptod[i]);
        }
        for (int A = int_ipf[0]; A <= int_ipt[0]; A++) {
            for (int B = (A == int_ipf[0] ? int_ipf[1] : 0); B <= (A == int_ipt[0] ? int_ipt[1]
                    : 255); B++) {
                for (int C = (B == int_ipf[1] ? int_ipf[2] : 0); C <= (B == int_ipt[1] ? int_ipt[2]
                        : 255); C++) {
                    for (int D = (C == int_ipf[2] ? int_ipf[3] : 0); D <= (C == int_ipt[2] ? int_ipt[3]
                            : 255); D++) {
                        ips.add(A + "." + B + "." + C + "." + D);
                    }
                }
            }
        }
        return ips;
    }

    /**
     * 把long类型的Ip转为一般Ip类型：xx.xx.xx.xx
     *
     * @param ip
     * @return
     */
    public static String longIpToStr(Long ip) {
        String s1 = String.valueOf((ip & 4278190080L) / 16777216L);
        String s2 = String.valueOf((ip & 16711680L) / 65536L);
        String s3 = String.valueOf((ip & 65280L) / 256L);
        String s4 = String.valueOf(ip & 255L);
        return s1 + "." + s2 + "." + s3 + "." + s4;
    }

    /**
     * 把xxx.xxx.xxx.xxx类型的转为long类型的IP
     *
     * @param ip 字符类型的IP
     * @return
     */
    public static Long strIpToLong(String ip) {
        Long ipLong = 0L;
        String ipTemp = ip;
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1);
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1);
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1);
        ipLong = ipLong * 256 + Long.parseLong(ipTemp);
        return ipLong;
    }

    /**
     * 根据掩码位获取掩码
     *
     * @param maskBit 掩码位数，如"28"、"30"
     * @return
     */
    public static String getMaskByMaskBit(String maskBit) {
        return StrUtil.isEmpty(maskBit) ? "error, maskBit is null !"
                : maskBitMap().get(maskBit);
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP（字符串型）
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的字符串表示
     */
    public static String getBeginIpStr(String ip, int maskBit) {
        return longIpToStr(getBeginIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP（Long型）
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的长整型表示
     */
    private static Long getBeginIpLong(String ip, int maskBit) {
        return strIpToLong(ip) & strIpToLong(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP（字符串型）
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的字符串表示
     */
    public static String getEndIpStr(String ip, int maskBit) {
        return longIpToStr(getEndIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP（Long型）
     * 注：此接口返回负数，请使用转成字符串后再转Long型
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的长整型表示
     */
    private static Long getEndIpLong(String ip, int maskBit) {
        return getBeginIpLong(ip, maskBit)
                + ~strIpToLong(getMaskByMaskBit(maskBit));
    }


    /**
     * 根据子网掩码转换为掩码位
     *
     * @param mask 掩码，例如xxx.xxx.xxx.xxx
     * @return 掩码位，例如32
     */
    public static int getMaskBitByMask(String mask) {
        StringBuffer sbf;
        String str;
        int inetmask = 0, count = 0;
        String[] ipList = mask.split("\\.");
        for (int n = 0; n < ipList.length; n++) {
            sbf = toBin(Integer.parseInt(ipList[n]));
            str = sbf.reverse().toString();
            count = 0;
            for (int i = 0; i < str.length(); i++) {
                i = str.indexOf('1', i);
                if (i == -1) {
                    break;
                }
                count++;
            }
            inetmask += count;
        }
        return inetmask;
    }

    /**
     * 计算子网大小
     *
     * @param maskBit 掩码位
     * @param isAll   true:全量地址，false:可用地址
     * @return 地址总数
     */
    public static int countByMaskBit(int maskBit, boolean isAll) {
        //如果是可用地址的情况，掩码位小于等于0或大于等于32，则可用地址为0
        boolean isZero = !isAll && (maskBit <= 0 || maskBit >= 32);
        if (isZero) {
            return 0;
        }
        if (isAll) {
            return (int) Math.pow(2, 32 - maskBit);
        } else {
            return (int) Math.pow(2, 32 - maskBit) - 2;
        }
    }

    private static StringBuffer toBin(int x) {
        StringBuffer result = new StringBuffer();
        result.append(x % 2);
        x /= 2;
        while (x > 0) {
            result.append(x % 2);
            x /= 2;
        }
        return result;
    }

    /**
     * 存储着所有的掩码位及对应的掩码 key:掩码位 value:掩码（x.x.x.x）
     *
     * @return
     */
    private static Map<String, String> maskBitMap() {
        Map<String, String> maskBit = new HashMap<>(32);
        maskBit.put("1", "128.0.0.0");
        maskBit.put("2", "192.0.0.0");
        maskBit.put("3", "224.0.0.0");
        maskBit.put("4", "240.0.0.0");
        maskBit.put("5", "248.0.0.0");
        maskBit.put("6", "252.0.0.0");
        maskBit.put("7", "254.0.0.0");
        maskBit.put("8", "255.0.0.0");
        maskBit.put("9", "255.128.0.0");
        maskBit.put("10", "255.192.0.0");
        maskBit.put("11", "255.224.0.0");
        maskBit.put("12", "255.240.0.0");
        maskBit.put("13", "255.248.0.0");
        maskBit.put("14", "255.252.0.0");
        maskBit.put("15", "255.254.0.0");
        maskBit.put("16", "255.255.0.0");
        maskBit.put("17", "255.255.128.0");
        maskBit.put("18", "255.255.192.0");
        maskBit.put("19", "255.255.224.0");
        maskBit.put("20", "255.255.240.0");
        maskBit.put("21", "255.255.248.0");
        maskBit.put("22", "255.255.252.0");
        maskBit.put("23", "255.255.254.0");
        maskBit.put("24", "255.255.255.0");
        maskBit.put("25", "255.255.255.128");
        maskBit.put("26", "255.255.255.192");
        maskBit.put("27", "255.255.255.224");
        maskBit.put("28", "255.255.255.240");
        maskBit.put("29", "255.255.255.248");
        maskBit.put("30", "255.255.255.252");
        maskBit.put("31", "255.255.255.254");
        maskBit.put("32", "255.255.255.255");
        return maskBit;
    }

    /**
     * 根据掩码位获取掩码
     *
     * @param maskBit
     * @return
     */
    public static String getMaskByMaskBit(int maskBit) {
        switch (maskBit) {
            case 1:
                return "128.0.0.0";
            case 2:
                return "192.0.0.0";
            case 3:
                return "224.0.0.0";
            case 4:
                return "240.0.0.0";
            case 5:
                return "248.0.0.0";
            case 6:
                return "252.0.0.0";
            case 7:
                return "254.0.0.0";
            case 8:
                return "255.0.0.0";
            case 9:
                return "255.128.0.0";
            case 10:
                return "255.192.0.0";
            case 11:
                return "255.224.0.0";
            case 12:
                return "255.240.0.0";
            case 13:
                return "255.248.0.0";
            case 14:
                return "255.252.0.0";
            case 15:
                return "255.254.0.0";
            case 16:
                return "255.255.0.0";
            case 17:
                return "255.255.128.0";
            case 18:
                return "255.255.192.0";
            case 19:
                return "255.255.224.0";
            case 20:
                return "255.255.240.0";
            case 21:
                return "255.255.248.0";
            case 22:
                return "255.255.252.0";
            case 23:
                return "255.255.254.0";
            case 24:
                return "255.255.255.0";
            case 25:
                return "255.255.255.128";
            case 26:
                return "255.255.255.192";
            case 27:
                return "255.255.255.224";
            case 28:
                return "255.255.255.240";
            case 29:
                return "255.255.255.248";
            case 30:
                return "255.255.255.252";
            case 31:
                return "255.255.255.254";
            case 32:
                return "255.255.255.255";
            default:
                return "";
        }
    }

    /**
     * 根据开始IP与结束IP计算掩码
     *
     * @param fromIp 开始IP
     * @param toIp   结束IP
     * @return 掩码x.x.x.x
     * @throws Exception
     */
    public static String getMaskByIpRange(String fromIp, String toIp) throws Exception {
        Long toIpLong = strIpToLong(toIp);
        Long fromIpLong = strIpToLong(fromIp);
        if (fromIpLong > toIpLong) {
            throw new Exception("开始IP大与结束IP");
        }
        String[] fromIpSplit = fromIp.split("\\.");
        String[] toIpSplit = toIp.split("\\.");
        StringBuilder mask = new StringBuilder();
        for (int i = 0; i < toIpSplit.length; i++) {
            mask.append(255 - Integer.parseInt(toIpSplit[i]) + Integer.parseInt(fromIpSplit[i])).append(".");
        }
        return mask.substring(0, mask.length() - 1);
    }

    /**
     * 计算IP区间有多少个IP
     *
     * @param fromIp 开始IP
     * @param toIp   结束IP
     * @return IP数量
     * @throws Exception
     */
    public static int countByIpRange(String fromIp, String toIp) throws Exception {
        Long toIpLong = strIpToLong(toIp);
        Long fromIpLong = strIpToLong(fromIp);
        if (fromIpLong > toIpLong) {
            throw new Exception("开始IP大与结束IP");
        }
        int count = 1;
        int[] fromIpSplit = Arrays.stream(fromIp.split("\\.")).mapToInt(Integer::parseInt).toArray();
        int[] toIpSplit = Arrays.stream(toIp.split("\\.")).mapToInt(Integer::parseInt).toArray();
        for (int i = fromIpSplit.length - 1; i >= 0; i--) {
            count += (toIpSplit[i] - fromIpSplit[i]) * Math.pow(256, fromIpSplit.length - i - 1);
        }
        return count;
    }
}
