package org.dromara.hutool.core.text;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.exceptions.HutoolException;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.placeholder.StrTemplate;
import org.dromara.hutool.core.text.placeholder.template.NamedPlaceholderStrTemplate;
import org.dromara.hutool.core.text.placeholder.template.SinglePlaceholderStrTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * test for {@link StrTemplate}
 *
 * @author emptypoint
 */
public class StrTemplateTest {

    @Test
    public void singlePlaceholderFormatTest() {
        //  默认值
        testSinglePlaceholderFormat("{}", '\\');

        // 自定义占位符
        testSinglePlaceholderFormat("?", '\\');

        //  自定义多个占位符
        testSinglePlaceholderFormat("$$$", '\\');

        // 自定义多个占位符和转义符
        testSinglePlaceholderFormat("$$$", '/');
    }

    @Test
    public void namedPlaceholderFormatSequenceTest() {
        String text = "select * from #[tableName] where id = #[id]";
        NamedPlaceholderStrTemplate strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.formatSequence("user", 1001)
        );
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.formatArraySequence(new String[]{"user", "1001"})
        );
        Assertions.assertEquals(
                "select * from 123 where id = 456",
                strTemplate.formatArraySequence(new int[]{123, 456})
        );
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.formatSequence(ListUtil.of("user", 1001))
        );
    }

    @Test
    public void namedPlaceholderFormatIndexedTest() {
        String text = "select * from #[1] where id = #[2]";
        NamedPlaceholderStrTemplate strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();

        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.formatIndexed("hutool", "user", 1001)
        );
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.formatArrayIndexed(new String[]{"hutool", "user", "1001"})
        );
        Assertions.assertEquals(
                "select * from 123 where id = 456",
                strTemplate.formatArrayIndexed(new int[]{666, 123, 456})
        );
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.formatIndexed(ListUtil.of("hutool", "user", 1001))
        );
        Assertions.assertEquals(
                "select * from user where id = ?",
                strTemplate.formatIndexed(ListUtil.of("hutool", "user"), idx -> "?")
        );
    }

    @Test
    public void namedPlaceholderFormatTest() {
        String text = "select * from #[tableName] where id = #[id]";
        NamedPlaceholderStrTemplate strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();

        Map<String, Object> map = MapUtil.<String, Object>builder().put("tableName", "user").put("id", 1001).build();
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.format(map)
        );
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.format((Object) map)
        );

        FormatEntity entity = new FormatEntity().setTableName("user").setId(1001);
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.format(entity)
        );
        entity = new FormatEntity().setTableName("user").setId(1001);
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.format(entity)
        );
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormatEntity {
        private String tableName;
        private Integer id;
    }

    @Test
    public void namedPlaceholderFormatDefaultValueTest() {
        String text = "i {a}{m} a {jvav} programmer";
        NamedPlaceholderStrTemplate.Builder strTemplate = StrTemplate.ofNamed(text)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE);
        Assertions.assertEquals(
                "i  a  programmer",
                strTemplate.defaultValue(s -> "")
                        .build()
                        .formatSequence()
        );
        Assertions.assertEquals(
                "i ?? a ? programmer",
                strTemplate.defaultValue(s -> "?")
                        .build()
                        .formatSequence()
        );
        Assertions.assertEquals(
                "i $$$$$$ a $$$ programmer",
                strTemplate.defaultValue(s -> "$$$")
                        .build()
                        .formatSequence()
        );

        text = "select * from #[tableName] where id = #[id]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]");
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.defaultValue(s -> "?")
                        .build()
                        .formatSequence("user", 1001)
        );
        Assertions.assertEquals(
                "select * from user where id = ?",
                strTemplate.defaultValue(s -> "?")
                        .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE)
                        .build()
                        .formatSequence("user")
        );
        Assertions.assertEquals(
                "select * from user where id = ?",
                strTemplate.defaultValue(s -> "?")
                        .build()
                        .formatArraySequence(new String[]{"user"})
        );
        Assertions.assertEquals(
                "select * from user where id = ?",
                strTemplate.defaultValue(s -> "?")
                        .build()
                        .formatSequence(ListUtil.of("user"))
        );

        text = "select * from #[1] where id = #[2]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE);
        Assertions.assertEquals(
                "select * from user where id = ?",
                strTemplate.defaultValue(s -> "?")
                        .build()
                        .formatIndexed("hutool", "user")
        );
        Assertions.assertEquals(
                "select * from user where id = ?",
                strTemplate.defaultValue(s -> "?")
                        .build()
                        .formatArrayIndexed(new String[]{"hutool", "user"})
        );
        Assertions.assertEquals(
                "select * from user where id = ?",
                strTemplate.defaultValue(s -> "?")
                        .build()
                        .formatIndexed(ListUtil.of("hutool", "user"))
        );
    }

    @Test
    public void namedPlaceholderEscapeTest() {
        Map<String, Object> map = MapUtil.<String, Object>builder().put("tableName", "user").put("id", 1001).build();
        // 转义符
        String text = "select * from \\#[tableName] where id = \\#[id]";
        NamedPlaceholderStrTemplate strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();
        Assertions.assertEquals(
                "select * from #[tableName] where id = #[id]",
                strTemplate.format(map)
        );
        text = "select * from \\#[tableName] where id = #[id\\]]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();
        Assertions.assertEquals(
                "select * from #[tableName] where id = 1001",
                strTemplate.format(MapUtil.<String, Object>builder().put("tableName", "user").put("id]", 1001).build())
        );

        // 转义 转义符
        text = "select * from \\\\#[tableName] where id = #[id]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();
        Assertions.assertEquals(
                "select * from \\user where id = 1001",
                strTemplate.format(map)
        );
        text = "select * from \\\\#[tableName] where id = \\\\#[id]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();
        Assertions.assertEquals(
                "select * from \\user where id = \\1001",
                strTemplate.format(map)
        );
        text = "select * from \\\\#[tableName] where id = #[id\\\\]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();
        Assertions.assertEquals(
                "select * from \\user where id = 1001",
                strTemplate.format(MapUtil.<String, Object>builder().put("tableName", "user").put("id\\", 1001).build())
        );
        text = "select * from #[tableName\\\\] where id = #[id\\\\]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").build();
        Assertions.assertEquals(
                "select * from user where id = 1001",
                strTemplate.format(MapUtil.<String, Object>builder().put("tableName\\", "user").put("id\\", 1001).build())
        );

        // 自定义 转义符
        text = "select * from /#[tableName] where id = /#[id]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").escape('/').build();
        Assertions.assertEquals(
                "select * from #[tableName] where id = #[id]",
                strTemplate.format(map)
        );
        text = "select * from //#[tableName] where id = //#[id]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").escape('/').build();
        Assertions.assertEquals(
                "select * from /user where id = /1001",
                strTemplate.format(map)
        );
        text = "select * from /#[tableName] where id = #[id/]]";
        strTemplate = StrTemplate.ofNamed(text).prefix("#[").suffix("]").escape('/').build();
        Assertions.assertEquals(
                "select * from #[tableName] where id = 1001",
                strTemplate.format(MapUtil.<String, Object>builder().put("tableName", "user").put("id]", 1001).build())
        );
    }

    private void testSinglePlaceholderFormat(String placeholder, char escape) {
        // 通常使用
        String commonTemplate = "this is " + placeholder + " for " + placeholder;
        SinglePlaceholderStrTemplate template = StrTemplate.of(commonTemplate)
                .placeholder(placeholder)
                .escape(escape)
                .build();


        // 普通使用
        Assertions.assertEquals("this is a for 666",
                template.format("a", 666)
        );
        Assertions.assertEquals("this is a for 666",
                template.format(ListUtil.of("a", 666))
        );
        Assertions.assertEquals("this is 123 for 456",
                template.formatArray(new int[]{123, 456})
        );
        Assertions.assertEquals("this is 123 for 456",
                template.formatArray(new Integer[]{123, 456})
        );

        // 转义占位符
        Assertions.assertEquals("this is " + placeholder + " for a",
                StrTemplate.of("this is " + escape + placeholder + " for " + placeholder)
                        .placeholder(placeholder)
                        .escape(escape)
                        .build()
                        .format("a", "b")
        );
        // 转义"转义符"
        Assertions.assertEquals("this is " + escape + "a for b",
                StrTemplate.of("this is " + escape + escape + placeholder + " for " + placeholder)
                        .placeholder(placeholder)
                        .escape(escape)
                        .build()
                        .format("a", "b")
        );
        // 填充null值
        Assertions.assertEquals("this is " + null + " for b",
                template.format(null, "b")
        );
        Assertions.assertEquals("this is a for null",
                template.format("a", null)
        );

        // 序列化参数 小于 占位符数量
        Assertions.assertEquals("this is a for " + placeholder,
                template.format("a")
        );


        SinglePlaceholderStrTemplate.Builder builder = StrTemplate.of(commonTemplate)
                .placeholder(placeholder)
                .escape(escape)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE);

        Assertions.assertEquals("this is a for ",
                builder.defaultValue("")
                        .build()
                        .format("a")
        );
        Assertions.assertEquals("this is a for 666",
                builder.defaultValue("666")
                        .build()
                        .format("a")
        );

        builder = StrTemplate.of(commonTemplate)
                .placeholder(placeholder)
                .escape(escape)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE);
        Assertions.assertEquals("this is a for ",
                builder.defaultValue(s -> "")
                        .build()
                        .format("a")
        );

        Assertions.assertEquals("this is a for 666",
                builder.defaultValue(s -> "666")
                        .build()
                        .format("a")
        );

        // 序列化参数 超过 占位符数量
        Assertions.assertEquals("this is a for b",
                builder.build()
                        .format("a", "b", "c")
        );

        // 残缺的占位符
        if (placeholder.length() >= 2) {
            Assertions.assertEquals("this is " + placeholder.charAt(0) + " for a",
                    StrTemplate.of("this is " + placeholder.charAt(0) + " for " + placeholder)
                            .placeholder(placeholder)
                            .escape(escape)
                            .build()
                            .format("a")
            );
            Assertions.assertEquals("this is " + placeholder.charAt(1) + " for a",
                    StrTemplate.of("this is " + placeholder.charAt(1) + " for " + placeholder)
                            .placeholder(placeholder)
                            .escape(escape)
                            .build()
                            .format("a")
            );
            Assertions.assertEquals("this is " + placeholder.charAt(0) + ' ' + placeholder.charAt(1) + " for a",
                    StrTemplate.of("this is " + placeholder.charAt(0) + ' ' + placeholder.charAt(1) + " for " + placeholder)
                            .placeholder(placeholder)
                            .escape(escape)
                            .build()
                            .format("a")
            );
        }
    }

    @Test
    public void isMatchesTest() {
        SinglePlaceholderStrTemplate strTemplate = StrTemplate.of("this is {} for {}").build();
        Assertions.assertTrue(strTemplate.isMatches("this is a for b"));
        Assertions.assertTrue(strTemplate.isMatches("this is aaa for 666"));
        Assertions.assertTrue(strTemplate.isMatches("this is a for b "));
        Assertions.assertTrue(strTemplate.isMatches("this is a x for b"));
        Assertions.assertTrue(strTemplate.isMatches("this is {} for {}"));
        Assertions.assertTrue(strTemplate.isMatches("this is { } for {}"));
        Assertions.assertTrue(strTemplate.isMatches("this is { } for { }"));
        Assertions.assertTrue(strTemplate.isMatches("this is  a for b"));
        Assertions.assertTrue(strTemplate.isMatches("this is  a for  b"));
        Assertions.assertTrue(strTemplate.isMatches("this is a  for b"));
        Assertions.assertTrue(strTemplate.isMatches("this is a for "));
        Assertions.assertTrue(strTemplate.isMatches("this is  for b"));
        Assertions.assertTrue(strTemplate.isMatches("this is  for "));

        Assertions.assertFalse(strTemplate.isMatches(""));
        Assertions.assertFalse(strTemplate.isMatches(" "));
        Assertions.assertFalse(strTemplate.isMatches("  \r\n \n "));
        Assertions.assertFalse(strTemplate.isMatches(" this is a for b"));
        Assertions.assertFalse(strTemplate.isMatches("this is a forb"));
        Assertions.assertFalse(strTemplate.isMatches("this  is a for b"));
        Assertions.assertFalse(strTemplate.isMatches("this are a for b"));
        Assertions.assertFalse(strTemplate.isMatches("that is a for b"));

        // 占位符在最前和最后
        strTemplate = StrTemplate.of("{}, this is for {}").build();
        Assertions.assertTrue(strTemplate.isMatches("Cleveland, this is for you"));
        Assertions.assertTrue(strTemplate.isMatches("Cleveland, this is for you "));
        Assertions.assertTrue(strTemplate.isMatches(" Cleveland, this is for you"));
        Assertions.assertTrue(strTemplate.isMatches("Cleveland, this is for  you "));
        Assertions.assertTrue(strTemplate.isMatches("Cleveland, this is for you ?"));
        Assertions.assertTrue(strTemplate.isMatches("Cleveland , this is for you"));
        Assertions.assertTrue(strTemplate.isMatches(":)Cleveland, this is for you"));

        Assertions.assertFalse(strTemplate.isMatches("Cleveland,  this is for you"));
        Assertions.assertFalse(strTemplate.isMatches("Cleveland, this  is for you"));
        Assertions.assertFalse(strTemplate.isMatches("Cleveland, this is  for you"));
        Assertions.assertFalse(strTemplate.isMatches("Cleveland, this is four you"));
        Assertions.assertFalse(strTemplate.isMatches("Cleveland, this are for you"));
        Assertions.assertFalse(strTemplate.isMatches("Cleveland, that is for you"));
    }

    @Test
    public void singlePlaceholderMatchesTest() {
        SinglePlaceholderStrTemplate strTemplate = StrTemplate.of("this is {} for {}").build();
        Assertions.assertEquals(ListUtil.of("a", "b"), strTemplate.matches("this is a for b"));
        Assertions.assertEquals(ListUtil.of("aaa", "666"), strTemplate.matches("this is aaa for 666"));
        Assertions.assertEquals(ListUtil.of("a", "b "), strTemplate.matches("this is a for b "));
        Assertions.assertEquals(ListUtil.of("a x", "b"), strTemplate.matches("this is a x for b"));
        Assertions.assertEquals(ListUtil.of("{}", "{}"), strTemplate.matches("this is {} for {}"));
        Assertions.assertEquals(ListUtil.of("{ }", "{}"), strTemplate.matches("this is { } for {}"));
        Assertions.assertEquals(ListUtil.of("{ }", "{ }"), strTemplate.matches("this is { } for { }"));
        Assertions.assertEquals(ListUtil.of(" a", "b"), strTemplate.matches("this is  a for b"));
        Assertions.assertEquals(ListUtil.of(" a", " b"), strTemplate.matches("this is  a for  b"));
        Assertions.assertEquals(ListUtil.of("a ", "b"), strTemplate.matches("this is a  for b"));
        Assertions.assertEquals(ListUtil.of("a", null), strTemplate.matches("this is a for "));
        Assertions.assertEquals(ListUtil.of(null, "b"), strTemplate.matches("this is  for b"));
        Assertions.assertEquals(ListUtil.of(null, null), strTemplate.matches("this is  for "));

        final List<Object> emptyList = Collections.emptyList();
        Assertions.assertEquals(emptyList, strTemplate.matches(""));
        Assertions.assertEquals(emptyList, strTemplate.matches(" "));
        Assertions.assertEquals(emptyList, strTemplate.matches("  \r\n \n "));
        Assertions.assertEquals(emptyList, strTemplate.matches(" this is a for b"));
        Assertions.assertEquals(emptyList, strTemplate.matches("this is a forb"));
        Assertions.assertEquals(emptyList, strTemplate.matches("this  is a for b"));
        Assertions.assertEquals(emptyList, strTemplate.matches("this are a for b"));
        Assertions.assertEquals(emptyList, strTemplate.matches("that is a for b"));

        strTemplate = StrTemplate.of("{}, this is for {}").build();
        Assertions.assertEquals(ListUtil.of("Cleveland", "you"), strTemplate.matches("Cleveland, this is for you"));
        Assertions.assertEquals(ListUtil.of(" Cleveland", "you"), strTemplate.matches(" Cleveland, this is for you"));
        Assertions.assertEquals(ListUtil.of("Cleveland ", "you"), strTemplate.matches("Cleveland , this is for you"));
        Assertions.assertEquals(ListUtil.of("Cleveland", "you "), strTemplate.matches("Cleveland, this is for you "));
        Assertions.assertEquals(ListUtil.of("Cleveland", " you"), strTemplate.matches("Cleveland, this is for  you"));
        Assertions.assertEquals(ListUtil.of("Cleveland", " you "), strTemplate.matches("Cleveland, this is for  you "));
        Assertions.assertEquals(ListUtil.of("Cleveland", "you ?"), strTemplate.matches("Cleveland, this is for you ?"));
        Assertions.assertEquals(ListUtil.of(":)Cleveland", "you:("), strTemplate.matches(":)Cleveland, this is for you:("));

        Assertions.assertEquals(emptyList, strTemplate.matches("Cleveland,  this is for you"));
        Assertions.assertEquals(emptyList, strTemplate.matches("Cleveland, this  is for you"));
        Assertions.assertEquals(emptyList, strTemplate.matches("Cleveland, this is  for you"));
        Assertions.assertEquals(emptyList, strTemplate.matches("Cleveland, this is four you"));
        Assertions.assertEquals(emptyList, strTemplate.matches("Cleveland, this are for you"));
        Assertions.assertEquals(emptyList, strTemplate.matches("Cleveland, that is for you"));
    }

    @Test
    public void namedPlaceholderMatchesSequenceTest() {
        NamedPlaceholderStrTemplate strTemplate = StrTemplate.ofNamed("this is {a} for {b}").build();
        Assertions.assertEquals(ListUtil.of("a", "b"), strTemplate.matchesSequence("this is a for b"));
        Assertions.assertEquals(ListUtil.of("aaa", "666"), strTemplate.matchesSequence("this is aaa for 666"));
        Assertions.assertEquals(ListUtil.of("a", "b "), strTemplate.matchesSequence("this is a for b "));
        Assertions.assertEquals(ListUtil.of("a x", "b"), strTemplate.matchesSequence("this is a x for b"));
        Assertions.assertEquals(ListUtil.of("{}", "{}"), strTemplate.matchesSequence("this is {} for {}"));
        Assertions.assertEquals(ListUtil.of("{ }", "{}"), strTemplate.matchesSequence("this is { } for {}"));
        Assertions.assertEquals(ListUtil.of("{ }", "{ }"), strTemplate.matchesSequence("this is { } for { }"));
        Assertions.assertEquals(ListUtil.of(" a", "b"), strTemplate.matchesSequence("this is  a for b"));
        Assertions.assertEquals(ListUtil.of(" a", " b"), strTemplate.matchesSequence("this is  a for  b"));
        Assertions.assertEquals(ListUtil.of("a ", "b"), strTemplate.matchesSequence("this is a  for b"));
        Assertions.assertEquals(ListUtil.of("a", null), strTemplate.matchesSequence("this is a for "));
        Assertions.assertEquals(ListUtil.of(null, "b"), strTemplate.matchesSequence("this is  for b"));
        Assertions.assertEquals(ListUtil.of(null, null), strTemplate.matchesSequence("this is  for "));

        final List<Object> emptyList = Collections.emptyList();
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence(""));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence(" "));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("  \r\n \n "));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence(" this is a for b"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("this is a forb"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("this  is a for b"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("this are a for b"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("that is a for b"));

        strTemplate = StrTemplate.ofNamed("{a}, this is for {b}").build();
        Assertions.assertEquals(ListUtil.of("Cleveland", "you"), strTemplate.matchesSequence("Cleveland, this is for you"));
        Assertions.assertEquals(ListUtil.of(" Cleveland", "you"), strTemplate.matchesSequence(" Cleveland, this is for you"));
        Assertions.assertEquals(ListUtil.of("Cleveland ", "you"), strTemplate.matchesSequence("Cleveland , this is for you"));
        Assertions.assertEquals(ListUtil.of("Cleveland", "you "), strTemplate.matchesSequence("Cleveland, this is for you "));
        Assertions.assertEquals(ListUtil.of("Cleveland", " you"), strTemplate.matchesSequence("Cleveland, this is for  you"));
        Assertions.assertEquals(ListUtil.of("Cleveland", " you "), strTemplate.matchesSequence("Cleveland, this is for  you "));
        Assertions.assertEquals(ListUtil.of("Cleveland", "you ?"), strTemplate.matchesSequence("Cleveland, this is for you ?"));
        Assertions.assertEquals(ListUtil.of(":)Cleveland", "you:("), strTemplate.matchesSequence(":)Cleveland, this is for you:("));

        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("Cleveland,  this is for you"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("Cleveland, this  is for you"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("Cleveland, this is  for you"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("Cleveland, this is four you"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("Cleveland, this are for you"));
        Assertions.assertEquals(emptyList, strTemplate.matchesSequence("Cleveland, that is for you"));
    }

    @Test
    public void namedPlaceholderMatchesIndexedTest() {
        NamedPlaceholderStrTemplate strTemplate = StrTemplate.ofNamed("this is {2} for {1}").build();
        Assertions.assertEquals(ListUtil.of(null, "b", "a"), strTemplate.matchesIndexed("this is a for b", null));
        Assertions.assertEquals(ListUtil.of(null, "666", "aaa"), strTemplate.matchesIndexed("this is aaa for 666", null));
        Assertions.assertEquals(ListUtil.of(null, "b", null), strTemplate.matchesIndexed("this is  for b", null));
        Assertions.assertEquals(ListUtil.of(null, null, "aaa"), strTemplate.matchesIndexed("this is aaa for ", null));
        Assertions.assertEquals(ListUtil.of(null, null, null), strTemplate.matchesIndexed("this is  for ", null));

        strTemplate = StrTemplate.ofNamed("this is {2} for {1}")
                .addFeatures(StrTemplate.Feature.MATCH_EMPTY_VALUE_TO_DEFAULT_VALUE)
                .build();
        Assertions.assertEquals(ListUtil.of(null, "b", "a"), strTemplate.matchesIndexed("this is a for b", idx -> "?"));
        Assertions.assertEquals(ListUtil.of(null, "666", "aaa"), strTemplate.matchesIndexed("this is aaa for 666", idx -> "?"));
        Assertions.assertEquals(ListUtil.of(null, "b", "?"), strTemplate.matchesIndexed("this is  for b", idx -> "?"));
        Assertions.assertEquals(ListUtil.of(null, "?", "aaa"), strTemplate.matchesIndexed("this is aaa for ", idx -> "?"));
        Assertions.assertEquals(ListUtil.of(null, "?", "?"), strTemplate.matchesIndexed("this is  for ", idx -> "?"));

        strTemplate = StrTemplate.ofNamed("this is {2} for {1}").build();
        final List<Object> emptyList = Collections.emptyList();
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("", null));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed(" ", null));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("  \r\n \n ", null));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed(" this is a for b", null));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("this is a forb", null));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("this  is a for b", null));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("this are a for b", null));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("that is a for b", null));

        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed(" this is a for b", idx -> "?"));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("this is a forb", idx -> "?"));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("this  is a for b", idx -> "?"));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("this are a for b", idx -> "?"));
        Assertions.assertEquals(emptyList, strTemplate.matchesIndexed("that is a for b", idx -> "?"));
    }


    @Test
    public void namedPlaceholderMatchesTest() {
        NamedPlaceholderStrTemplate strTemplate = StrTemplate.ofNamed("this is {tableName} for {id}").build();
        Supplier<Map<String, String>> mapSupplier = HashMap::new;
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", "666").build(), strTemplate.matches("this is aaa for 666", mapSupplier));
        Assertions.assertEquals(MapUtil.builder("tableName", null).put("id", "666").build(), strTemplate.matches("this is  for 666", mapSupplier));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", null).build(), strTemplate.matches("this is aaa for ", mapSupplier));
        Assertions.assertEquals(MapUtil.builder("tableName", null).put("id", null).build(), strTemplate.matches("this is  for ", mapSupplier));
        Assertions.assertEquals(Collections.emptyMap(), strTemplate.matches("", mapSupplier));


        Supplier<FormatEntity> beanSupplier = FormatEntity::new;
        Assertions.assertEquals(new FormatEntity("aaa", 666), strTemplate.matches("this is aaa for 666", beanSupplier));
        Assertions.assertEquals(new FormatEntity(null, 666), strTemplate.matches("this is  for 666", beanSupplier));
        Assertions.assertEquals(new FormatEntity("aaa", null), strTemplate.matches("this is aaa for ", beanSupplier));
        Assertions.assertEquals(new FormatEntity(null, null), strTemplate.matches("this is  for ", beanSupplier));
        Assertions.assertEquals(new FormatEntity(), strTemplate.matches("", beanSupplier));
    }

    @Test
    public void featureTest() {
        // 通常使用
        String commonTemplate = "this is {tableName} for {id}";
        // ##### 使用新的策略 替换 默认策略 #####
        NamedPlaceholderStrTemplate template = StrTemplate.ofNamed(commonTemplate)
                .features(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_EMPTY, StrTemplate.Feature.MATCH_IGNORE_EMPTY_VALUE)
                .build();
        testFeature(template);

        // 添加新策略，互斥的策略则算作设置新策略，旧策略失效
        template = StrTemplate.ofNamed(commonTemplate)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_EMPTY, StrTemplate.Feature.MATCH_IGNORE_DEFAULT_VALUE, StrTemplate.Feature.MATCH_IGNORE_EMPTY_VALUE)
                .build();
        testFeature(template);

        // ##### 删除策略 #####
        NamedPlaceholderStrTemplate template2 = StrTemplate.ofNamed(commonTemplate)
                .removeFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_WHOLE_PLACEHOLDER)
                .build();
        Assertions.assertEquals("this is aaa for 666", template2.format(MapUtil.builder("tableName", "aaa").put("id", "666").build()));
        Assertions.assertThrows(HutoolException.class, () -> template2.format(MapUtil.builder("tableName", "aaa").build()));

        // ##### 空字符串策略 #####
        template = StrTemplate.ofNamed(commonTemplate)
                // 解析时，空字符串 转为 null值
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_EMPTY, StrTemplate.Feature.MATCH_EMPTY_VALUE_TO_NULL)
                .build();
        Assertions.assertEquals("this is aaa for ", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", null).build(), template.matches("this is aaa for null"));

        // 解析时，空字符串 转为 默认值
        template = StrTemplate.ofNamed(commonTemplate)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_EMPTY, StrTemplate.Feature.MATCH_EMPTY_VALUE_TO_DEFAULT_VALUE)
                .defaultValue("?")
                .build();
        Assertions.assertEquals("this is aaa for ", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", "?").build(), template.matches("this is aaa for "));

        // 默认值 为 空字符串，解析时，空字符串 转为 默认值
        template = StrTemplate.ofNamed(commonTemplate)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_EMPTY, StrTemplate.Feature.MATCH_EMPTY_VALUE_TO_DEFAULT_VALUE)
                .defaultValue("")
                .build();
        Assertions.assertEquals("this is aaa for ", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", "").build(), template.matches("this is aaa for "));

        // ##### null值策略 #####
        // 解析时，null字符串 转为 null值
        template = StrTemplate.ofNamed(commonTemplate)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_NULL, StrTemplate.Feature.MATCH_NULL_STR_TO_NULL)
                .build();
        Assertions.assertEquals("this is aaa for null", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", null).build(), template.matches("this is aaa for null"));
        // 格式化时，null值 转为 默认值 ；解析时，null字符串 转为 null值
        template = StrTemplate.ofNamed(commonTemplate)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE, StrTemplate.Feature.MATCH_NULL_STR_TO_NULL)
                .defaultValue("null")
                .build();
        Assertions.assertEquals("this is aaa for null", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", null).build(), template.matches("this is aaa for null"));

        // 解析时，忽略 null字符串
        template = StrTemplate.ofNamed(commonTemplate)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_NULL, StrTemplate.Feature.MATCH_IGNORE_NULL_STR)
                .build();
        Assertions.assertEquals("this is aaa for null", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").build(), template.matches("this is aaa for null"));
        // 格式化时，null值 转为 默认值 ；解析时，忽略 null字符串
        template = StrTemplate.ofNamed(commonTemplate)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE, StrTemplate.Feature.MATCH_IGNORE_NULL_STR)
                .defaultValue("null")
                .build();
        Assertions.assertEquals("this is aaa for null", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").build(), template.matches("this is aaa for null"));

        // 解析时，null字符串 依然为 "null"字符串
        template = StrTemplate.ofNamed(commonTemplate)
                .addFeatures(StrTemplate.Feature.FORMAT_MISSING_KEY_PRINT_NULL, StrTemplate.Feature.MATCH_KEEP_NULL_STR)
                .build();
        Assertions.assertEquals("this is aaa for null", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", "null").build(), template.matches("this is aaa for null"));
    }

    private void testFeature(NamedPlaceholderStrTemplate template) {
        // 格式化
        Assertions.assertEquals("this is aaa for 666", template.format(MapUtil.builder("tableName", "aaa").put("id", "666").build()));
        Assertions.assertEquals("this is aaa for ", template.format(MapUtil.builder("tableName", "aaa").build()));
        Assertions.assertEquals("this is  for 666", template.format(MapUtil.builder("id", "666").build()));
        Assertions.assertEquals("this is  for ", template.format(MapUtil.builder().build()));

        // 解析
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").put("id", "666").build(), template.matches("this is aaa for 666"));
        Assertions.assertEquals(MapUtil.builder("tableName", "aaa").build(), template.matches("this is aaa for "));
        Assertions.assertEquals(MapUtil.builder("id", "666").build(), template.matches("this is  for 666"));
        Assertions.assertEquals(MapUtil.builder().build(), template.matches("this is  for "));
    }
}
