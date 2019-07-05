package cn.hutool.bloomfilter;

import static org.mockito.Matchers.anyDouble;

import com.diffblue.deeptestutils.Reflector;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;

@RunWith(PowerMockRunner.class)
public class BitSetBloomFilterTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();
  @Rule public final Timeout globalTimeout = new Timeout(10000);

  /* testedClasses: BitSetBloomFilter */
  // Test written by Diffblue Cover.
  @PrepareForTest({BitSetBloomFilter.class, Math.class})
  @Test
  public void addInputNotNullOutputFalse() throws Exception {

    // Setup mocks
    PowerMockito.mockStatic(Math.class);

    // Arrange
    final BitSetBloomFilter objectUnderTest = new BitSetBloomFilter(0, 0, 0);
    final String str = "Bar";
    PowerMockito.when(Math.ceil(anyDouble())).thenReturn(-0.5);

    // Act
    final boolean retval = objectUnderTest.add(str);

    // Assert result
    Assert.assertFalse(retval);
  }

  // Test written by Diffblue Cover.

  @Test
  public void addInputNotNullOutputFalse2() throws InvocationTargetException {

    // Arrange
    final BitSetBloomFilter objectUnderTest =
        (BitSetBloomFilter)Reflector.getInstance("cn.hutool.bloomfilter.BitSetBloomFilter");
    Reflector.setField(objectUnderTest, "hashFunctionNumber", 1);
    Reflector.setField(objectUnderTest, "addedElements", 0);
    Reflector.setField(objectUnderTest, "bitSetSize", 1);
    final BitSet bitSet = new BitSet(2);
    bitSet.set(0, true);
    Reflector.setField(objectUnderTest, "bitSet", bitSet);
    final String str = "\'";

    // Act
    final boolean retval = objectUnderTest.add(str);

    // Assert result
    Assert.assertFalse(retval);
  }

  // Test written by Diffblue Cover.
  @PrepareForTest({BitSetBloomFilter.class, Math.class})
  @Test
  public void constructorInputZeroZeroZeroOutputVoid() throws Exception {

    // Setup mocks
    PowerMockito.mockStatic(Math.class);

    // Arrange
    final int c = 0;
    final int n = 0;
    final int k = 0;
    PowerMockito.when(Math.ceil(anyDouble())).thenReturn(-0.5).thenReturn(-0.5);

    // Act, creating object to test constructor
    final BitSetBloomFilter objectUnderTest = new BitSetBloomFilter(c, n, k);

    // Method returns void, testing that no exception is thrown
  }

  // Test written by Diffblue Cover.
  @PrepareForTest({BitSetBloomFilter.class, Math.class})
  @Test
  public void containsInputNotNullOutputTrue() throws Exception {

    // Setup mocks
    PowerMockito.mockStatic(Math.class);

    // Arrange
    final BitSetBloomFilter objectUnderTest = new BitSetBloomFilter(0, 0, 0);
    final String str = "foo";
    PowerMockito.when(Math.ceil(anyDouble())).thenReturn(-0.5);

    // Act
    final boolean retval = objectUnderTest.contains(str);

    // Assert result
    Assert.assertTrue(retval);
  }

  // Test written by Diffblue Cover.

  @Test
  public void containsInputNotNullOutputTrue2() throws InvocationTargetException {

    // Arrange
    final BitSetBloomFilter objectUnderTest =
        (BitSetBloomFilter)Reflector.getInstance("cn.hutool.bloomfilter.BitSetBloomFilter");
    Reflector.setField(objectUnderTest, "hashFunctionNumber", 1);
    Reflector.setField(objectUnderTest, "addedElements", 0);
    Reflector.setField(objectUnderTest, "bitSetSize", 1);
    final BitSet bitSet = new BitSet();
    bitSet.set(0);
    Reflector.setField(objectUnderTest, "bitSet", bitSet);
    final String str = "2";

    // Act
    final boolean retval = objectUnderTest.contains(str);

    // Assert result
    Assert.assertTrue(retval);
  }

  // Test written by Diffblue Cover.

  @Test
  public void createHashesInputNotNullPositiveOutput1() {

    // Arrange
    final String str = "1";
    final int hashNumber = 1;

    // Act
    final int[] retval = BitSetBloomFilter.createHashes(str, hashNumber);

    // Assert result
    Assert.assertArrayEquals(new int[] {49}, retval);
  }

  // Test written by Diffblue Cover.

  @Test
  public void createHashesInputNotNullZeroOutput0() {

    // Arrange
    final String str = "foo";
    final int hashNumber = 0;

    // Act
    final int[] retval = BitSetBloomFilter.createHashes(str, hashNumber);

    // Assert result
    Assert.assertArrayEquals(new int[] {}, retval);
  }

  // Test written by Diffblue Cover.
  @PrepareForTest({BitSetBloomFilter.class, Math.class})
  @Test
  public void getFalsePositiveProbabilityOutputZero() throws Exception, InvocationTargetException {

    // Setup mocks
    PowerMockito.mockStatic(Math.class);

    // Arrange
    final BitSetBloomFilter objectUnderTest =
        (BitSetBloomFilter)Reflector.getInstance("cn.hutool.bloomfilter.BitSetBloomFilter");
    Reflector.setField(objectUnderTest, "hashFunctionNumber", -1_073_741_825);
    Reflector.setField(objectUnderTest, "addedElements", 1);
    Reflector.setField(objectUnderTest, "bitSetSize", 0);
    final BitSet bitSet = new BitSet();
    Reflector.setField(objectUnderTest, "bitSet", bitSet);
    PowerMockito.when(Math.ceil(anyDouble())).thenReturn(-0x1.000038ep-974 /* -6.26305e-294 */);
    PowerMockito.when(Math.pow(anyDouble(), anyDouble())).thenReturn(0.0);
    PowerMockito.when(Math.exp(anyDouble())).thenReturn(-0.0);

    // Act
    final double retval = objectUnderTest.getFalsePositiveProbability();

    // Assert result
    Assert.assertEquals(0.0, retval, 0.0);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullPositiveOutputPositive() {

    // Arrange
    final String str = "/";
    final int k = 5;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(177_620, retval);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullPositiveOutputPositive2() {

    // Arrange
    final String str = "/";
    final int k = 7;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(47, retval);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullPositiveOutputPositive3() {

    // Arrange
    final String str = "/";
    final int k = 6;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(47, retval);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullPositiveOutputPositive4() {

    // Arrange
    final String str = "/";
    final int k = 4;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(47, retval);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullPositiveOutputPositive5() {

    // Arrange
    final String str = "/";
    final int k = 3;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(47, retval);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullPositiveOutputPositive6() {

    // Arrange
    final String str = "/";
    final int k = 2;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(47, retval);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullPositiveOutputPositive7() {

    // Arrange
    final String str = "/";
    final int k = 1;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(787_808_287, retval);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullPositiveOutputZero() {

    // Arrange
    final String str = "foo";
    final int k = 14;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(0, retval);
  }

  // Test written by Diffblue Cover.
  @Test
  public void hashInputNotNullZeroOutputPositive() {

    // Arrange
    final String str = ",";
    final int k = 0;

    // Act
    final int retval = BitSetBloomFilter.hash(str, k);

    // Assert result
    Assert.assertEquals(44, retval);
  }
}

