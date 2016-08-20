/**
  * Created by siddharthachandra on 8/17/16.
  */
package StitchHelpers

import org.scalatest._

class ChromosomeStitchHelpersTest extends FunSpec {

  describe("ChromosomeStitchHelpers.accumulatorDNASequenceForLeftOverlap"){
    it("returns the combined DNA sequence by appending reference fragment name and sequence to candidate"){
      val candidate = DNASequence("foo", "123456")
      val reference = DNASequence("bar", "3456789")
      val overlapLength = "3456".length

      val expected = DNASequence("foo|bar","123456789")
      val actual = ChromosomeStitchHelpers.accumulatorDNASequenceForLeftOverlap(reference, candidate, overlapLength)

      assert(actual === expected)
    }
  }
}
