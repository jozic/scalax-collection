import scoverage._

ScoverageKeys.coverageMinimum := 100

ScoverageKeys.coverageFailOnMinimum := true

ScoverageKeys.coverageHighlighting := true

addCommandAlias("scoverage", ";clean;coverage;test;coverageReport")
