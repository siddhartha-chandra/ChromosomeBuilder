/**
  * Created by siddharthachandra on 8/17/16.
  */
package StitchHelpers

import StitchHelpers.ChromosomeStitchHelpers._
import org.scalatest._

class ChromosomeStitchHelpersTest extends FunSpec {

  describe("ChromosomeStitchHelpers.accumulatorDNASequenceForLeftOverlap"){
    it("returns the combined DNA sequence by appending reference fragment name and sequence to candidate"){
      val candidate = DNASequence("foo", "123456")
      val reference = DNASequence("bar", "3456789")
      val overlapLength = "3456".length

      val expected = DNASequence("foo|bar","123456789")
      val actual = accumulatorDNASequenceForLeftOverlap(reference, candidate, overlapLength)

      assert(actual === expected)
    }
  }

  describe("ChromosomeStitchHelpers.accumulatorDNASequenceForRightOverlap"){
    it("returns the combined DNA sequence by appending reference fragment name and sequence to candidate"){
      val candidate = DNASequence("bar", "3456789")
      val reference = DNASequence("foo", "123456")
      val overlapLength = "3456".length

      val expected = DNASequence("foo|bar","123456789")
      val actual = accumulatorDNASequenceForRightOverlap(reference, candidate, overlapLength)

      assert(actual === expected)
    }
  }


  describe("ChromosomeStitchHelpers.findLeftOverlap"){
    it("returns list of overlapping texts for the beginning portion of the reference DNA sequence and " +
      "ending portion of all candidate DNA sequences"){

      val reference =  DNASequence("bar", "3456789")
      val candidates = List(
        DNASequence("foo1", "123456"),
        DNASequence("foo2", "1234"),
        DNASequence("foo3", "0123"),
        DNASequence("foo4", "456789"),
        DNASequence("foo5", "4567"),
        DNASequence("foo6", "012")
      )

      val expected = List(Some("3456"), Some("34"), Some("3"))
      val actual = findLeftOverlap(candidates, reference)

      assert(expected === actual)
    }

    it("returns Nil if candidates are Nil"){

      val reference =  DNASequence("bar", "3456789")
      val candidates = Nil
      val expected = Nil
      val actual = findLeftOverlap(candidates, reference)

      assert(expected === actual)
    }
  }

  describe("ChromosomeStitchHelpers.findRightOverlap"){
    it("returns list of overlapping texts for the ending portion of the reference DNA sequence and " +
      "beginning portion of all candidate DNA sequences"){

      val reference =  DNASequence("foo", "123456")
      val candidates = List(
        DNASequence("bar1", "3456789"),
        DNASequence("bar2", "56789"),
        DNASequence("bar3", "6789"),
        DNASequence("bar4", "789"),
        DNASequence("bar5", "2345"),
        DNASequence("bar6", "12345")
      )

      val expected = List(Some("3456"), Some("56"), Some("6"))
      val actual = findRightOverlap(candidates, reference)

      assert(expected === actual)
    }

    it("returns Nil if candidates are Nil"){

      val reference =  DNASequence("bar", "3456789")
      val candidates = Nil
      val expected = Nil
      val actual = findRightOverlap(candidates, reference)

      assert(expected === actual)
    }
  }

  describe("ChromosomeStitchHelpers.getMatchingTextForLeftOverLap"){

    val overlapText = "456"
    it("returns true if DNA sequence ends with overlap text"){
      val dnaSequence = "123456"
      assert(doesDNASequenceContainLeftOverlapTxt(dnaSequence, overlapText) === true)
    }

    it("returns false if DNA sequence does not end with overlap text"){
      val dnaSequence1 = "12345"
      val dnaSequence2 = "123"
      val dnaSequence3 = "1234567"
      val dnaSequences = List(dnaSequence1, dnaSequence2, dnaSequence3)
      assert(dnaSequences.forall(doesDNASequenceContainLeftOverlapTxt(_, overlapText)) === false)
    }
  }

  describe("ChromosomeStitchHelpers.getMatchingTextForRightOverLap"){

    val overlapText = "123"
    it("returns true if DNA sequence starts with overlap text"){
      val dnaSequence = "123456"
      assert(doesDNASequenceContainRightOverlapTxt(dnaSequence, overlapText) === true)
    }

    it("returns false if DNA sequence does not start with overlap text"){
      val dnaSequence1 = "9123"
      val dnaSequence2 = "871234"
      val dnaSequence3 = "875"
      val dnaSequences = List(dnaSequence1, dnaSequence2, dnaSequence3)
      assert(dnaSequences.forall(doesDNASequenceContainLeftOverlapTxt(_, overlapText)) === false)
    }
  }
//  describe("ChromosomeStitchHelpers.getOverlap")
//  describe("ChromosomeStitchHelpers.constructChromosome")

}
