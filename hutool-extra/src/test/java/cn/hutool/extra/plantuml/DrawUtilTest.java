package cn.hutool.extra.plantuml;

import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;
import java.io.*;

/**
 * PlantUML代码图表生成工具类单元测试
 *
 * @author LGXTvT
 */
public class DrawUtilTest {

	@Test
	@Ignore
	public void toBase64Test() {
		String img = DrawUtil.toBase64(
			"@startuml\n" +
				"skinparam backgroundColor #EEEBDC\n" +
				"skinparam handwritten true\n" +
				"skinparam sequence {\n" +
				"ArrowColor DeepSkyBlue\n" +
				"ActorBorderColor DeepSkyBlue\n" +
				"LifeLineBorderColor blue\n" +
				"LifeLineBackgroundColor #A9DCDF\n" +
				"ParticipantBorderColor DeepSkyBlue\n" +
				"ParticipantBackgroundColor DodgerBlue\n" +
				"ParticipantFontName Impact\n" +
				"ParticipantFontSize 17\n" +
				"ParticipantFontColor #A9DCDF\n" +
				"ActorBackgroundColor aqua\n" +
				"ActorFontColor DeepSkyBlue\n" +
				"ActorFontSize 17\n" +
				"ActorFontName Aapex\n" +
				"}\n" +
				"actor User\n" +
				"participant \"First Class\" as A\n" +
				"participant \"Second Class\" as B\n" +
				"participant \"Last Class\" as C\n" +
				"User -> A: DoWork\n" +
				"activate A\n" +
				"A -> B: Create Request\n" +
				"activate B\n" +
				"B -> C: DoWork\n" +
				"activate C\n" +
				"C --> B: WorkDone\n" +
				"destroy C\n" +
				"B --> A: Request Created\n" +
				"deactivate B\n" +
				"A --> User: Done\n" +
				"deactivate A\n" +
				"@enduml"
		);
		Console.log(img);
	}

	@Test
	@Ignore
	public void toFileTest() {
		DrawUtil.toFile(
			"@startuml\n" +
				"autonumber\n" +
				"\n" +
				"Alice -> Bob: Authentication Request\n" +
				"Bob --> Alice: Authentication Response\n" +
				"\n" +
				"Alice -> Bob: Another authentication Request\n" +
				"Alice <-- Bob: another authentication Response\n" +
				"@enduml\n",
			new File("D://demo.png")
		);
	}

	@Test
	@Ignore
	public void toOutputStreamTest() {
		try (FileOutputStream fileOutputStream = new FileOutputStream("D://demo1.png")) {
			DrawUtil.toOutputStream(
				"@startuml\n" +
					"autonumber\n" +
					"\n" +
					"Alice -> Bob: Authentication Request\n" +
					"Bob --> Alice: Authentication Response\n" +
					"\n" +
					"Alice -> Bob: Another authentication Request\n" +
					"Alice <-- Bob: another authentication Response\n" +
					"@enduml\n",
				fileOutputStream
			);
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

}
