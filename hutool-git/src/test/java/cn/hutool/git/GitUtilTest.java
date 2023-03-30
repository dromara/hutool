package cn.hutool.git;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.io.FileUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.junit.RepositoryTestCase;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GitUtilTest extends RepositoryTestCase {

	@Test(expected = RefNotFoundException.class)
	public void error_checkoutEmptyRepository() throws GitAPIException, IOException {
		GitUtil util = GitUtil.testing(db);
		util.checkout("test");
	}

	/**
	 * 完整测试git流程
	 */
	@Test
	public void fullGitflow() throws GitAPIException, IOException, URISyntaxException {
		//初始化
		GitUtil util = GitUtil.testing(db);
		Git git = util.getGit();
		String folder = git.getRepository().getDirectory().getParent();
		//随便修改点东西
		testModify(folder);
		//判断状态
		Status status = git.status().call();
		Set<String> untrackedFileList = status.getUntracked();
		boolean match = untrackedFileList.stream().anyMatch(s -> s.equals("test.txt"));
		assertTrue(match);
		//提交
		util.commit("测试提交");
		//判断状态
		status = git.status().call();
		assertTrue(status.isClean());//工作目录是clean的
		List<RevCommit> logs = IterUtil.toList(git.log().setMaxCount(5).call());
		assertEquals(1, logs.size());//提交记录有1条
		RevCommit commit = logs.get(0);
		assertEquals("测试提交", commit.getFullMessage());
		//准备推送的仓库
		Repository db2 = createWorkRepository();
		FileBasedConfig db1Config = db.getConfig();
		RemoteConfig db1RemoteConfig = new RemoteConfig(db1Config, "test");
		URIish uri = new URIish(db2.getDirectory().toURI().toURL());
		db1RemoteConfig.addURI(uri);
		db1RemoteConfig.update(db1Config);
		db1Config.save();
		Git git2 = new Git(db2);
		//推送
		util.push("test", "test");
		//判断状态
		String db1BranchName = git.branchList().call().get(0).getName();
		assertEquals("refs/heads/master", db1BranchName);
		String db2BranchName = git2.branchList().call().get(0).getName();
		assertEquals("refs/heads/test", db2BranchName);
		String db1CommitId = git.branchList().call().get(0).getObjectId().getName();
		String db2CommitId = git2.branchList().call().get(0).getObjectId().getName();
		assertEquals(db1CommitId, db2CommitId);
		assertEquals(commit.getId(), db2.resolve(db2CommitId));
		//打标签
		util.tag("v1.0.0");
		//判断状态
		List<Ref> tagList = git.tagList().call();
		assertEquals(1, tagList.size());
		List<Ref> tagList2 = git2.tagList().call();
		assertEquals(0, tagList2.size());
		//推送标签
		util.pushTags("test");
		//判断状态
		tagList2 = git2.tagList().call();
		assertEquals(1, tagList2.size());
		String tagName1 = tagList.get(0).getName();
		assertEquals("refs/tags/v1.0.0", tagName1);
		String tagName2 = tagList2.get(0).getName();
		assertEquals(tagName1, tagName2);
		String commitId1 = tagList.get(0).getObjectId().getName();
		String commitId2 = tagList2.get(0).getObjectId().getName();
		assertEquals(commitId1, commitId2);
		assertEquals(tagList.get(0).getObjectId(), db2.resolve(commitId2));
	}

	private void testModify(String folder) {
		FileUtil.writeUtf8String("111", folder + File.separator + "test.txt");
	}
}
