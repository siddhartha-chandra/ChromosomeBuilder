import ChromosomeStitchHelpers.constructChromosome

import scala.io.Source

case class DNASequence(fragmentName: String, sequence: String)
case class OverlapCandidate(candidate: DNASequence, overlapText:String)

object Main{

  type Threshold = Double
  //as more than half the length for overlap is the criteria
  val threshold:Threshold = 0.5
  val currentDirectory =  new java.io.File(".").getCanonicalPath
  val fileName = "data_set.txt"
  val filePath = s"$currentDirectory/src/main/resources/$fileName"

  def main(args: Array[String]): Unit = {
    val lines = Source.fromFile(filePath).getLines()
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