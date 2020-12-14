package cn.hutool.crypto.test;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.BCUtil;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.junit.Test;

public class BCUtilTest {

	/**
	 * 密钥生成来自：https://i.goto327.top/CryptTools/SM2.aspx?tdsourcetag=s_pctim_aiomsg
	 */
	@Test
	public void createECPublicKeyParametersTest() {
		String x = "706AD9DAA3E5CEAC3DA59F583429E8043BAFC576BE10092C4EA4D8E19846CA62";
		String y = "F7E938B02EED7280277493B8556E5B01CB436E018A562DFDC53342BF41FDF728";

		final ECPublicKeyParameters keyParameters = BCUtil.toSm2Params(x, y);
		Assert.notNull(keyParameters);
	}

	@Test
	public void createECPrivateKeyParametersTest() {
		String privateKeyHex = "5F6CA5BB044C40ED2355F0372BF72A5B3AE6943712F9FDB7C1FFBAECC06F3829";

		final ECPrivateKeyParameters keyParameters = BCUtil.toSm2Params(privateKeyHex);
		Assert.notNull(keyParameters);
	}
}
