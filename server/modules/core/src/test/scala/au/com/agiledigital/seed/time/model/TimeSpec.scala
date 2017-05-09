package au.com.agiledigital.seed.time.model

import java.io.File
import java.nio.file.Paths

import au.com.agiledigital.rest.tests.BaseSpec
import org.specs2.matcher.FileMatchers
import org.specs2.specification.core.Fragments
import play.api.libs.json._

import scala.io.Source
import scala.language.postfixOps

/**
  * Contains unit tests for [[Time]].
  */
class TimeSpec extends BaseSpec with FileMatchers {

  override val ff = fragmentFactory

  "Writing to JSON" should {
    "write as expected" in {
      // Given a system time.
      val time = TimeDataProvider.time

      // When it is converted to JSON.
      val actual = Json.toJson(time)

      // Then it should match the expected JSON.
      actual must_=== Json.parse(timeAsJson)
    }
  }

  "Reading from JSON" should {
    "read as expected" in {
      // Given a time as JSON.

      // When it is converted to a time.
      val actual = Json.parse(timeAsJson).as[Time]

      // Then it should match the expected time.
      actual must_=== TimeDataProvider.time
    }
  }

  val timeAsJson =
    """
      | {
      |    "date":"system date",
      |    "time":"system date time",
      |    "millis":1000
      | }
    """.stripMargin

  // Tests driven by test data.

  val inputFiles = new File(getClass.getResource("/au/com/agiledigital/seed/time/test_data/format").toURI).
    listFiles().
    filter(file =>
      file.getName.endsWith("_input.json"))

  val dataDrivenFragments = inputFiles map { file =>
    ff.example(file.getName, {
      // Given an input file.
      file aka s"Input file [$file]" must exist

      // And the expected output file.
      val expectedOutputFilename = file.getName.replace("_input.json", "_output.json")
      val expectedOutputFile = Paths.get(file.getParentFile.toURI).resolve(expectedOutputFilename).toFile
      expectedOutputFile aka s"Output file for input file [$file]" must exist

      // When the input file is read.
      val inputJsonAsString = Source.fromFile(file).mkString
      val inputJson = Json.parse(inputJsonAsString)

      // Then it should be a valid Time
      val inputTimeResult = inputJson.validate[Time]
      inputTimeResult must beAnInstanceOf[JsSuccess[Time]]
      val JsSuccess(inputTime, _) = inputTimeResult

      // Then when the input time is written to JSON.
      val outputTimeJson = Json.toJson(inputTime)

      // Then it should match the JSON read from the output file.
      val expectedJson = Json.parse(Source.fromFile(expectedOutputFile).mkString)

      outputTimeJson must_=== expectedJson
    })
  } flatMap { fragment =>
    Seq(ff.break, ff.tab, fragment, ff.backtab)
  }

  addFragments(Fragments(ff.text("Reading JSON from a data file should be round-trippable") +: dataDrivenFragments: _*))

}
