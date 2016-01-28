import org.scoverage.coveralls.CoverallsPlugin._
import scoverage.ScoverageSbtPlugin._

CoverallsKeys.coverallsTokenFile := Some("./token.txt")

ScoverageKeys.coverageMinimum := 95

ScoverageKeys.coverageFailOnMinimum := true

ScoverageKeys.coverageHighlighting := true
