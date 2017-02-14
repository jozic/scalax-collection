import scoverage._

CoverallsKeys.coverallsTokenFile := Some("./token.txt")

ScoverageKeys.coverageMinimum := 95

ScoverageKeys.coverageFailOnMinimum := true

ScoverageKeys.coverageHighlighting := true

addCommandAlias("scoverage", ";clean;coverage;test;coverageReport")
