package Core

import Core.Utils._
import StitchHelpers.DNASequence
import org.scalatest.FunSpec

import scala.io.Source

/**
  * Created by siddharthachandra on 8/20/16.
  */
class UtilsTest extends FunSpec {

  describe("Utils.isCandidateQualified") {
    val overlapTextOpt = Some("over")
    it("returns true if overlap length wrt text is greater than threshold"){
      val text = "overlap"
      val actual = isCandidateQualified(overlapTextOpt.get, text)
      assert(actual === true)
    }
    it("returns false if overlap length wrt text is less than threshold"){
      val text = "overlapping"
      val actual = isCandidateQualified(overlapTextOpt.get, text)
      assert(actual === false)
    }
    it("returns true if overlap length of DNA sequence wrt text is greater than threshold"){
      val candidateOpt = Some(DNASequence("dna1","overlap"))
      val actual = isCandidateQualified(overlapTextOpt,candidateOpt)
      assert(actual === true)
    }
    it("returns false if overlap length of DNA sequence wrt text is less than threshold"){
      val candidateOpt = Some(DNASequence("dna1","overlapping"))
      val actual = isCandidateQualified(overlapTextOpt,candidateOpt)
      assert(actual === false)
    }
  }

  describe("Utils.getDNASequences") {
    it("returns empty list if the input provided is an empty iterator"){
      val input = Iterator()
      assert(getDNASequences(input) === Nil)
    }

    it("returns a list of DNA sequences if the input provided has strings in the FASTA format"){

      val input = {
        val currentDirectory =  new java.io.File(".").getCanonicalPath
        val fileName = "sampleData.txt"
        val filePath = s"$currentDirectory/src/main/resources/$fileName"
        Source.fromFile(filePath).getLines()
      }

      val actual = List(
        DNASequence("Frag_56", "ATTAGACCTG"),
        DNASequence("Frag_57", "CCTGCCGGAA"),
        DNASequence("Frag_58", "AGACCTGCCG"),
        DNASequence("Frag_59", "GCCGGAATAC")
      )

      assert(actual === getDNASequences(input))
    }
  }

}
