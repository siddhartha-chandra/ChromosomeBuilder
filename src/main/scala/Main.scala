import ChromosomeStitchHelpers.constructChromosome

import scala.io.Source

case class DNASequence(fragmentName: String, sequence: String)
case class OverlapCandidate(candidate: DNASequence, overlapText:String)

object Main{

  type Threshold = Double
  //as more than half the length for overlap is the criteria
  val threshold:Threshold = 0.5
  //todo: fix path to relative
  val filename = "/Users/siddharthachandra/Documents/programming/driver/ChromosomeBuilder/src/main/resources/data_set.txt"

  def main(args: Array[String]): Unit = {
    val lines = Source.fromFile(filename).getLines()
    val DNASequences = Utils.getDNASequences(lines)
    val constructedChromosome = constructChromosome(DNASequences)

    println(
      s"""
      ~~Constructed Chromosome~~

      Fragments:
      ${constructedChromosome.fragmentName}

      Sequence:
      ${constructedChromosome.sequence}
      """)

  }
}