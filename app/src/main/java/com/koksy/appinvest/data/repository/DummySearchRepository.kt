package com.koksy.appinvest.data.repository

import com.koksy.appinvest.domain.model.AssetType
import com.koksy.appinvest.domain.model.ComparisonMetricGroup
import com.koksy.appinvest.domain.model.FinancialMetric
import com.koksy.appinvest.domain.model.MetricComparisonRow
import com.koksy.appinvest.domain.model.MetricComparisonSet
import com.koksy.appinvest.domain.model.MetricComparisonValue
import com.koksy.appinvest.domain.model.PricePoint
import com.koksy.appinvest.domain.model.StockDetail
import com.koksy.appinvest.domain.model.StockSearchResult
import com.koksy.appinvest.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DummySearchRepository : SearchRepository {
    override fun searchStocks(query: String): Flow<List<StockSearchResult>> {
        val normalizedQuery = query.trim().lowercase()
        val results = if (normalizedQuery.isBlank()) {
            searchResults
        } else {
            searchResults.filter { item ->
                item.symbol.lowercase().contains(normalizedQuery) ||
                    item.name.lowercase().contains(normalizedQuery) ||
                    item.market.lowercase().contains(normalizedQuery) ||
                    item.themeTags.any { it.lowercase().contains(normalizedQuery) }
            }
        }
        return flowOf(results)
    }

    override fun observeStockDetail(symbol: String): Flow<StockDetail?> {
        return flowOf(details[symbol])
    }

    override fun observeMetricComparison(
        symbol: String,
        group: ComparisonMetricGroup,
    ): Flow<MetricComparisonSet> {
        val peerUniverse = peerUniverses[symbol] ?: peerUniverses.getValue("NVDA")
        return flowOf(peerUniverse.toComparisonSet(selectedSymbol = symbol, group = group))
    }

    private val searchResults = listOf(
        StockSearchResult(
            symbol = "NVDA",
            name = "NVIDIA",
            market = "NASDAQ",
            assetType = AssetType.STOCK,
            themeTags = listOf("AI 반도체", "데이터센터"),
        ),
        StockSearchResult(
            symbol = "005930",
            name = "삼성전자",
            market = "KOSPI",
            assetType = AssetType.STOCK,
            themeTags = listOf("메모리", "온디바이스 AI"),
        ),
        StockSearchResult(
            symbol = "OKLO",
            name = "Oklo",
            market = "NYSE",
            assetType = AssetType.STOCK,
            themeTags = listOf("SMR", "원전"),
        ),
        StockSearchResult(
            symbol = "IONQ",
            name = "IonQ",
            market = "NYSE",
            assetType = AssetType.STOCK,
            themeTags = listOf("양자컴퓨터"),
        ),
        StockSearchResult(
            symbol = "SCHD",
            name = "Schwab US Dividend Equity ETF",
            market = "NYSE Arca",
            assetType = AssetType.ETF,
            themeTags = listOf("배당", "ETF"),
        ),
        StockSearchResult(
            symbol = "TIGER 미국S&P500",
            name = "TIGER 미국S&P500",
            market = "ETF",
            assetType = AssetType.ETF,
            themeTags = listOf("미국지수", "ETF"),
        ),
    )

    private val details = mapOf(
        "NVDA" to StockDetail(
            symbol = "NVDA",
            name = "NVIDIA",
            market = "NASDAQ",
            assetType = AssetType.STOCK,
            price = "$1,142.32",
            changeRate = 2.84,
            marketCap = "$2.8T",
            tradingValue = "$42.1B",
            themeTags = listOf("AI 반도체", "데이터센터", "GPU"),
            chartPoints = pricePoints(910.0, 946.0, 972.0, 1_010.0, 1_052.0, 1_095.0, 1_142.0),
            financials = listOf(
                FinancialMetric("PER", "64.2x", "고성장 프리미엄"),
                FinancialMetric("매출 성장", "+126%", "최근 분기 YoY"),
                FinancialMetric("영업이익률", "64%", "데이터센터 중심"),
                FinancialMetric("배당", "0.02%", "성장주 성격"),
            ),
            summary = "AI 가속기 수요와 데이터센터 투자 사이클의 중심 종목입니다.",
        ),
        "005930" to StockDetail(
            symbol = "005930",
            name = "삼성전자",
            market = "KOSPI",
            assetType = AssetType.STOCK,
            price = "78,400원",
            changeRate = 1.17,
            marketCap = "468조원",
            tradingValue = "1.9조원",
            themeTags = listOf("메모리", "HBM", "온디바이스 AI"),
            chartPoints = pricePoints(71_200.0, 72_600.0, 74_100.0, 73_800.0, 75_900.0, 77_200.0, 78_400.0),
            financials = listOf(
                FinancialMetric("PER", "18.7x", "업황 회복 반영"),
                FinancialMetric("PBR", "1.4x", "대형주 평균권"),
                FinancialMetric("영업이익률", "15%", "반도체 회복 구간"),
                FinancialMetric("배당수익률", "1.8%", "분기 배당"),
            ),
            summary = "메모리 업황 회복과 HBM 공급 확대 기대가 핵심입니다.",
        ),
        "OKLO" to StockDetail(
            symbol = "OKLO",
            name = "Oklo",
            market = "NYSE",
            assetType = AssetType.STOCK,
            price = "$14.88",
            changeRate = 12.46,
            marketCap = "$1.7B",
            tradingValue = "$1.4B",
            themeTags = listOf("SMR", "원전", "전력"),
            chartPoints = pricePoints(9.4, 9.9, 10.8, 11.2, 12.6, 13.1, 14.9),
            financials = listOf(
                FinancialMetric("매출", "Pre-revenue", "개발 단계"),
                FinancialMetric("현금", "$290M", "프로젝트 자금"),
                FinancialMetric("변동성", "High", "테마 민감"),
                FinancialMetric("배당", "-", "성장 투자"),
            ),
            summary = "SMR 전력 공급 기대와 데이터센터 전력 수요가 맞물린 고변동성 테마주입니다.",
        ),
        "IONQ" to StockDetail(
            symbol = "IONQ",
            name = "IonQ",
            market = "NYSE",
            assetType = AssetType.STOCK,
            price = "$41.21",
            changeRate = 8.73,
            marketCap = "$8.9B",
            tradingValue = "$920M",
            themeTags = listOf("양자컴퓨터", "클라우드"),
            chartPoints = pricePoints(29.2, 31.1, 30.4, 34.8, 37.0, 39.6, 41.2),
            financials = listOf(
                FinancialMetric("매출 성장", "+77%", "초기 시장"),
                FinancialMetric("R&D 비중", "High", "기술 투자"),
                FinancialMetric("현금", "$410M", "운영 자금"),
                FinancialMetric("배당", "-", "성장 투자"),
            ),
            summary = "양자컴퓨팅 상용화 기대를 가장 직접적으로 반영하는 순수 테마 종목입니다.",
        ),
        "SCHD" to StockDetail(
            symbol = "SCHD",
            name = "Schwab US Dividend Equity ETF",
            market = "NYSE Arca",
            assetType = AssetType.ETF,
            price = "$79.12",
            changeRate = 0.42,
            marketCap = "$58B AUM",
            tradingValue = "$612M",
            themeTags = listOf("배당", "ETF", "현금흐름"),
            chartPoints = pricePoints(73.8, 74.1, 75.6, 76.4, 77.2, 78.6, 79.1),
            financials = listOf(
                FinancialMetric("분배수익률", "3.5%", "분기 분배"),
                FinancialMetric("보수", "0.06%", "저비용 ETF"),
                FinancialMetric("보유종목", "100+", "미국 배당주"),
                FinancialMetric("스타일", "Value", "현금흐름 중심"),
            ),
            summary = "배당 성장과 낮은 보수를 선호하는 장기 투자자에게 자주 비교되는 ETF입니다.",
        ),
        "TIGER 미국S&P500" to StockDetail(
            symbol = "TIGER 미국S&P500",
            name = "TIGER 미국S&P500",
            market = "ETF",
            assetType = AssetType.ETF,
            price = "18,965원",
            changeRate = 0.31,
            marketCap = "4.1조원 AUM",
            tradingValue = "842억원",
            themeTags = listOf("미국지수", "ETF", "장기투자"),
            chartPoints = pricePoints(17_420.0, 17_760.0, 18_020.0, 18_180.0, 18_430.0, 18_720.0, 18_965.0),
            financials = listOf(
                FinancialMetric("추종지수", "S&P 500", "미국 대형주"),
                FinancialMetric("보수", "0.07%", "국내 상장 ETF"),
                FinancialMetric("환노출", "Unhedged", "달러 영향"),
                FinancialMetric("분배", "분기", "상품 기준 확인"),
            ),
            summary = "국내 계좌에서 미국 대표지수에 투자할 수 있는 핵심 지수형 ETF입니다.",
        ),
    )

    private val metricDefinitions = mapOf(
        ComparisonMetricGroup.BASIC to listOf(
            MetricDefinition("marketCap", "시가총액/AUM", "주식은 시가총액, ETF는 순자산 규모", true),
            MetricDefinition("tradingValue", "거래대금", "최근 거래 유동성", true),
            MetricDefinition("per", "PER", "낮을수록 이익 대비 가격 부담 완화", false),
            MetricDefinition("pbr", "PBR", "낮을수록 장부가 대비 가격 부담 완화", false),
            MetricDefinition("psr", "PSR", "낮을수록 매출 대비 가격 부담 완화", false),
        ),
        ComparisonMetricGroup.CORE_FINANCIALS to listOf(
            MetricDefinition("revenueGrowth", "매출 성장률", "전년 대비 성장률", true),
            MetricDefinition("operatingMargin", "영업이익률", "본업의 수익성", true),
            MetricDefinition("roe", "ROE", "자본 대비 이익 창출력", true),
            MetricDefinition("epsGrowth", "EPS 성장률", "주당순이익 개선 속도", true),
        ),
        ComparisonMetricGroup.STABILITY to listOf(
            MetricDefinition("debtRatio", "부채비율", "낮을수록 재무 부담 완화", false),
            MetricDefinition("currentRatio", "유동비율", "단기 지급 여력", true),
            MetricDefinition("interestCoverage", "이자보상배율", "이자비용 감당 능력", true),
        ),
        ComparisonMetricGroup.CASH_FLOW to listOf(
            MetricDefinition("operatingCashFlow", "영업현금흐름", "영업 활동에서 만든 현금", true),
            MetricDefinition("freeCashFlow", "FCF", "투자 후 남는 현금", true),
            MetricDefinition("fcfYield", "FCF Yield", "시가총액 대비 잉여현금흐름", true),
        ),
        ComparisonMetricGroup.DIVIDEND_ETF to listOf(
            MetricDefinition("dividendYield", "배당수익률", "현재 가격 기준 배당 매력", true),
            MetricDefinition("dividendGrowth", "배당성장률", "최근 배당 증가 속도", true),
            MetricDefinition("expenseRatio", "보수율", "ETF 비용, 낮을수록 유리", false),
            MetricDefinition("aum", "AUM", "ETF 순자산 규모", true),
            MetricDefinition("trackingError", "추적오차", "지수 추종 오차, 낮을수록 유리", false),
        ),
    )

    private val aiDataCenterPeers = PeerUniverse(
        label = "AI/데이터센터 대형주",
        peers = listOf(
            PeerProfile(
                symbol = "NVDA",
                name = "NVIDIA",
                metrics = metrics(
                    "marketCap" to metric("$2.8T", 2_800.0),
                    "tradingValue" to metric("$42.1B", 42.1),
                    "per" to metric("64.2x", 64.2),
                    "pbr" to metric("51.0x", 51.0),
                    "psr" to metric("34.0x", 34.0),
                    "revenueGrowth" to metric("+126%", 126.0),
                    "operatingMargin" to metric("64%", 64.0),
                    "roe" to metric("91%", 91.0),
                    "epsGrowth" to metric("+461%", 461.0),
                    "debtRatio" to metric("18%", 18.0),
                    "currentRatio" to metric("3.5x", 3.5),
                    "interestCoverage" to metric("240x", 240.0),
                    "operatingCashFlow" to metric("$28.1B", 28.1),
                    "freeCashFlow" to metric("$27.0B", 27.0),
                    "fcfYield" to metric("1.0%", 1.0),
                    "dividendYield" to metric("0.02%", 0.02),
                    "dividendGrowth" to metric("0%", 0.0),
                ),
            ),
            PeerProfile(
                symbol = "AMD",
                name = "AMD",
                metrics = metrics(
                    "marketCap" to metric("$260B", 260.0),
                    "tradingValue" to metric("$8.7B", 8.7),
                    "per" to metric("52.0x", 52.0),
                    "pbr" to metric("4.4x", 4.4),
                    "psr" to metric("10.2x", 10.2),
                    "revenueGrowth" to metric("+2%", 2.0),
                    "operatingMargin" to metric("7%", 7.0),
                    "roe" to metric("2%", 2.0),
                    "epsGrowth" to metric("+14%", 14.0),
                    "debtRatio" to metric("5%", 5.0),
                    "currentRatio" to metric("2.5x", 2.5),
                    "interestCoverage" to metric("38x", 38.0),
                    "operatingCashFlow" to metric("$1.7B", 1.7),
                    "freeCashFlow" to metric("$1.2B", 1.2),
                    "fcfYield" to metric("0.5%", 0.5),
                    "dividendYield" to metric("-", null),
                    "dividendGrowth" to metric("-", null),
                ),
            ),
            PeerProfile(
                symbol = "AVGO",
                name = "Broadcom",
                metrics = metrics(
                    "marketCap" to metric("$710B", 710.0),
                    "tradingValue" to metric("$6.2B", 6.2),
                    "per" to metric("38.0x", 38.0),
                    "pbr" to metric("12.8x", 12.8),
                    "psr" to metric("16.3x", 16.3),
                    "revenueGrowth" to metric("+43%", 43.0),
                    "operatingMargin" to metric("46%", 46.0),
                    "roe" to metric("58%", 58.0),
                    "epsGrowth" to metric("+20%", 20.0),
                    "debtRatio" to metric("88%", 88.0),
                    "currentRatio" to metric("1.3x", 1.3),
                    "interestCoverage" to metric("8.9x", 8.9),
                    "operatingCashFlow" to metric("$18.3B", 18.3),
                    "freeCashFlow" to metric("$17.5B", 17.5),
                    "fcfYield" to metric("2.5%", 2.5),
                    "dividendYield" to metric("1.3%", 1.3),
                    "dividendGrowth" to metric("+12%", 12.0),
                ),
            ),
            PeerProfile(
                symbol = "MSFT",
                name = "Microsoft",
                metrics = metrics(
                    "marketCap" to metric("$3.2T", 3_150.0),
                    "tradingValue" to metric("$12.4B", 12.4),
                    "per" to metric("36.0x", 36.0),
                    "pbr" to metric("11.1x", 11.1),
                    "psr" to metric("13.0x", 13.0),
                    "revenueGrowth" to metric("+17%", 17.0),
                    "operatingMargin" to metric("44%", 44.0),
                    "roe" to metric("36%", 36.0),
                    "epsGrowth" to metric("+21%", 21.0),
                    "debtRatio" to metric("31%", 31.0),
                    "currentRatio" to metric("1.2x", 1.2),
                    "interestCoverage" to metric("48x", 48.0),
                    "operatingCashFlow" to metric("$118B", 118.0),
                    "freeCashFlow" to metric("$70B", 70.0),
                    "fcfYield" to metric("2.2%", 2.2),
                    "dividendYield" to metric("0.7%", 0.7),
                    "dividendGrowth" to metric("+10%", 10.0),
                ),
            ),
        ),
    )

    private val memoryPeers = PeerUniverse(
        label = "메모리/파운드리 반도체",
        peers = listOf(
            PeerProfile(
                symbol = "005930",
                name = "삼성전자",
                metrics = metrics(
                    "marketCap" to metric("$350B", 350.0),
                    "tradingValue" to metric("$1.4B", 1.4),
                    "per" to metric("18.7x", 18.7),
                    "pbr" to metric("1.4x", 1.4),
                    "psr" to metric("1.9x", 1.9),
                    "revenueGrowth" to metric("+16%", 16.0),
                    "operatingMargin" to metric("15%", 15.0),
                    "roe" to metric("8.2%", 8.2),
                    "epsGrowth" to metric("+52%", 52.0),
                    "debtRatio" to metric("26%", 26.0),
                    "currentRatio" to metric("2.6x", 2.6),
                    "interestCoverage" to metric("18x", 18.0),
                    "operatingCashFlow" to metric("$33B", 33.0),
                    "freeCashFlow" to metric("$6.4B", 6.4),
                    "fcfYield" to metric("1.8%", 1.8),
                    "dividendYield" to metric("1.8%", 1.8),
                    "dividendGrowth" to metric("+1%", 1.0),
                ),
            ),
            PeerProfile(
                symbol = "000660",
                name = "SK hynix",
                metrics = metrics(
                    "marketCap" to metric("$108B", 108.0),
                    "tradingValue" to metric("$1.0B", 1.0),
                    "per" to metric("12.1x", 12.1),
                    "pbr" to metric("2.1x", 2.1),
                    "psr" to metric("2.5x", 2.5),
                    "revenueGrowth" to metric("+74%", 74.0),
                    "operatingMargin" to metric("28%", 28.0),
                    "roe" to metric("18%", 18.0),
                    "epsGrowth" to metric("+140%", 140.0),
                    "debtRatio" to metric("78%", 78.0),
                    "currentRatio" to metric("1.7x", 1.7),
                    "interestCoverage" to metric("7.5x", 7.5),
                    "operatingCashFlow" to metric("$12.8B", 12.8),
                    "freeCashFlow" to metric("$2.0B", 2.0),
                    "fcfYield" to metric("1.9%", 1.9),
                    "dividendYield" to metric("0.6%", 0.6),
                    "dividendGrowth" to metric("0%", 0.0),
                ),
            ),
            PeerProfile(
                symbol = "MU",
                name = "Micron",
                metrics = metrics(
                    "marketCap" to metric("$140B", 140.0),
                    "tradingValue" to metric("$3.4B", 3.4),
                    "per" to metric("15.8x", 15.8),
                    "pbr" to metric("2.4x", 2.4),
                    "psr" to metric("4.0x", 4.0),
                    "revenueGrowth" to metric("+58%", 58.0),
                    "operatingMargin" to metric("18%", 18.0),
                    "roe" to metric("11%", 11.0),
                    "epsGrowth" to metric("+118%", 118.0),
                    "debtRatio" to metric("37%", 37.0),
                    "currentRatio" to metric("2.7x", 2.7),
                    "interestCoverage" to metric("9.2x", 9.2),
                    "operatingCashFlow" to metric("$8.1B", 8.1),
                    "freeCashFlow" to metric("$1.4B", 1.4),
                    "fcfYield" to metric("1.0%", 1.0),
                    "dividendYield" to metric("0.4%", 0.4),
                    "dividendGrowth" to metric("+4%", 4.0),
                ),
            ),
            PeerProfile(
                symbol = "TSM",
                name = "TSMC",
                metrics = metrics(
                    "marketCap" to metric("$780B", 780.0),
                    "tradingValue" to metric("$2.7B", 2.7),
                    "per" to metric("28.4x", 28.4),
                    "pbr" to metric("6.8x", 6.8),
                    "psr" to metric("10.0x", 10.0),
                    "revenueGrowth" to metric("+30%", 30.0),
                    "operatingMargin" to metric("43%", 43.0),
                    "roe" to metric("27%", 27.0),
                    "epsGrowth" to metric("+39%", 39.0),
                    "debtRatio" to metric("24%", 24.0),
                    "currentRatio" to metric("2.4x", 2.4),
                    "interestCoverage" to metric("42x", 42.0),
                    "operatingCashFlow" to metric("$45B", 45.0),
                    "freeCashFlow" to metric("$16B", 16.0),
                    "fcfYield" to metric("2.1%", 2.1),
                    "dividendYield" to metric("1.4%", 1.4),
                    "dividendGrowth" to metric("+8%", 8.0),
                ),
            ),
        ),
    )

    private val smrPeers = PeerUniverse(
        label = "SMR/전력 인프라",
        peers = listOf(
            PeerProfile(
                symbol = "OKLO",
                name = "Oklo",
                metrics = metrics(
                    "marketCap" to metric("$1.7B", 1.7),
                    "tradingValue" to metric("$1.4B", 1.4),
                    "per" to metric("-", null),
                    "pbr" to metric("5.6x", 5.6),
                    "psr" to metric("-", null),
                    "revenueGrowth" to metric("Pre", null),
                    "operatingMargin" to metric("-", null),
                    "roe" to metric("-42%", -42.0),
                    "epsGrowth" to metric("-", null),
                    "debtRatio" to metric("2%", 2.0),
                    "currentRatio" to metric("8.2x", 8.2),
                    "interestCoverage" to metric("-", null),
                    "operatingCashFlow" to metric("-$80M", -0.08),
                    "freeCashFlow" to metric("-$90M", -0.09),
                    "fcfYield" to metric("-5.3%", -5.3),
                    "dividendYield" to metric("-", null),
                    "dividendGrowth" to metric("-", null),
                ),
            ),
            PeerProfile(
                symbol = "SMR",
                name = "NuScale",
                metrics = metrics(
                    "marketCap" to metric("$1.2B", 1.2),
                    "tradingValue" to metric("$350M", 0.35),
                    "per" to metric("-", null),
                    "pbr" to metric("6.8x", 6.8),
                    "psr" to metric("41x", 41.0),
                    "revenueGrowth" to metric("+24%", 24.0),
                    "operatingMargin" to metric("-", null),
                    "roe" to metric("-53%", -53.0),
                    "epsGrowth" to metric("-", null),
                    "debtRatio" to metric("8%", 8.0),
                    "currentRatio" to metric("5.1x", 5.1),
                    "interestCoverage" to metric("-", null),
                    "operatingCashFlow" to metric("-$110M", -0.11),
                    "freeCashFlow" to metric("-$120M", -0.12),
                    "fcfYield" to metric("-10.0%", -10.0),
                    "dividendYield" to metric("-", null),
                    "dividendGrowth" to metric("-", null),
                ),
            ),
            PeerProfile(
                symbol = "CCJ",
                name = "Cameco",
                metrics = metrics(
                    "marketCap" to metric("$22B", 22.0),
                    "tradingValue" to metric("$500M", 0.5),
                    "per" to metric("73.0x", 73.0),
                    "pbr" to metric("3.2x", 3.2),
                    "psr" to metric("9.1x", 9.1),
                    "revenueGrowth" to metric("+29%", 29.0),
                    "operatingMargin" to metric("17%", 17.0),
                    "roe" to metric("4.8%", 4.8),
                    "epsGrowth" to metric("+62%", 62.0),
                    "debtRatio" to metric("18%", 18.0),
                    "currentRatio" to metric("2.8x", 2.8),
                    "interestCoverage" to metric("24x", 24.0),
                    "operatingCashFlow" to metric("$400M", 0.4),
                    "freeCashFlow" to metric("$200M", 0.2),
                    "fcfYield" to metric("0.9%", 0.9),
                    "dividendYield" to metric("0.3%", 0.3),
                    "dividendGrowth" to metric("+5%", 5.0),
                ),
            ),
            PeerProfile(
                symbol = "CEG",
                name = "Constellation",
                metrics = metrics(
                    "marketCap" to metric("$95B", 95.0),
                    "tradingValue" to metric("$1.3B", 1.3),
                    "per" to metric("30.0x", 30.0),
                    "pbr" to metric("7.1x", 7.1),
                    "psr" to metric("3.5x", 3.5),
                    "revenueGrowth" to metric("+4%", 4.0),
                    "operatingMargin" to metric("17%", 17.0),
                    "roe" to metric("24%", 24.0),
                    "epsGrowth" to metric("+68%", 68.0),
                    "debtRatio" to metric("68%", 68.0),
                    "currentRatio" to metric("1.5x", 1.5),
                    "interestCoverage" to metric("6.7x", 6.7),
                    "operatingCashFlow" to metric("$4.2B", 4.2),
                    "freeCashFlow" to metric("$2.0B", 2.0),
                    "fcfYield" to metric("2.1%", 2.1),
                    "dividendYield" to metric("0.7%", 0.7),
                    "dividendGrowth" to metric("+8%", 8.0),
                ),
            ),
        ),
    )

    private val quantumPeers = PeerUniverse(
        label = "양자컴퓨팅",
        peers = listOf(
            PeerProfile(
                symbol = "IONQ",
                name = "IonQ",
                metrics = metrics(
                    "marketCap" to metric("$8.9B", 8.9),
                    "tradingValue" to metric("$920M", 0.92),
                    "per" to metric("-", null),
                    "pbr" to metric("9.4x", 9.4),
                    "psr" to metric("145x", 145.0),
                    "revenueGrowth" to metric("+77%", 77.0),
                    "operatingMargin" to metric("-", null),
                    "roe" to metric("-39%", -39.0),
                    "epsGrowth" to metric("-", null),
                    "debtRatio" to metric("4%", 4.0),
                    "currentRatio" to metric("7.9x", 7.9),
                    "interestCoverage" to metric("-", null),
                    "operatingCashFlow" to metric("-$95M", -0.095),
                    "freeCashFlow" to metric("-$105M", -0.105),
                    "fcfYield" to metric("-1.2%", -1.2),
                    "dividendYield" to metric("-", null),
                    "dividendGrowth" to metric("-", null),
                ),
            ),
            PeerProfile(
                symbol = "RGTI",
                name = "Rigetti",
                metrics = metrics(
                    "marketCap" to metric("$2.4B", 2.4),
                    "tradingValue" to metric("$410M", 0.41),
                    "per" to metric("-", null),
                    "pbr" to metric("7.2x", 7.2),
                    "psr" to metric("120x", 120.0),
                    "revenueGrowth" to metric("+36%", 36.0),
                    "operatingMargin" to metric("-", null),
                    "roe" to metric("-68%", -68.0),
                    "epsGrowth" to metric("-", null),
                    "debtRatio" to metric("12%", 12.0),
                    "currentRatio" to metric("3.4x", 3.4),
                    "interestCoverage" to metric("-", null),
                    "operatingCashFlow" to metric("-$74M", -0.074),
                    "freeCashFlow" to metric("-$80M", -0.08),
                    "fcfYield" to metric("-3.3%", -3.3),
                    "dividendYield" to metric("-", null),
                    "dividendGrowth" to metric("-", null),
                ),
            ),
            PeerProfile(
                symbol = "QBTS",
                name = "D-Wave",
                metrics = metrics(
                    "marketCap" to metric("$1.8B", 1.8),
                    "tradingValue" to metric("$280M", 0.28),
                    "per" to metric("-", null),
                    "pbr" to metric("8.5x", 8.5),
                    "psr" to metric("72x", 72.0),
                    "revenueGrowth" to metric("+42%", 42.0),
                    "operatingMargin" to metric("-", null),
                    "roe" to metric("-71%", -71.0),
                    "epsGrowth" to metric("-", null),
                    "debtRatio" to metric("35%", 35.0),
                    "currentRatio" to metric("2.1x", 2.1),
                    "interestCoverage" to metric("-", null),
                    "operatingCashFlow" to metric("-$58M", -0.058),
                    "freeCashFlow" to metric("-$63M", -0.063),
                    "fcfYield" to metric("-3.5%", -3.5),
                    "dividendYield" to metric("-", null),
                    "dividendGrowth" to metric("-", null),
                ),
            ),
            PeerProfile(
                symbol = "IBM",
                name = "IBM",
                metrics = metrics(
                    "marketCap" to metric("$175B", 175.0),
                    "tradingValue" to metric("$1.2B", 1.2),
                    "per" to metric("21.0x", 21.0),
                    "pbr" to metric("7.3x", 7.3),
                    "psr" to metric("2.8x", 2.8),
                    "revenueGrowth" to metric("+3%", 3.0),
                    "operatingMargin" to metric("16%", 16.0),
                    "roe" to metric("34%", 34.0),
                    "epsGrowth" to metric("+6%", 6.0),
                    "debtRatio" to metric("250%", 250.0),
                    "currentRatio" to metric("0.9x", 0.9),
                    "interestCoverage" to metric("9.5x", 9.5),
                    "operatingCashFlow" to metric("$13.9B", 13.9),
                    "freeCashFlow" to metric("$11.2B", 11.2),
                    "fcfYield" to metric("6.4%", 6.4),
                    "dividendYield" to metric("3.8%", 3.8),
                    "dividendGrowth" to metric("+1%", 1.0),
                ),
            ),
        ),
    )

    private val dividendEtfPeers = PeerUniverse(
        label = "미국 배당 ETF",
        peers = listOf(
            etfPeer("SCHD", "SCHD", "$58B", 58.0, "$612M", 612.0, "15.0x", 15.0, "2.9x", 2.9, "2.1x", 2.1, "+6%", 6.0, "22%", 22.0, "18%", 18.0, "+8%", 8.0, "3.5%", 3.5, "+10%", 10.0, "0.06%", 0.06, "$58B", 58.0, "0.08%", 0.08),
            etfPeer("VIG", "Vanguard Dividend Appreciation", "$92B", 92.0, "$310M", 310.0, "24.0x", 24.0, "5.2x", 5.2, "3.3x", 3.3, "+8%", 8.0, "23%", 23.0, "26%", 26.0, "+9%", 9.0, "1.8%", 1.8, "+8%", 8.0, "0.06%", 0.06, "$92B", 92.0, "0.06%", 0.06),
            etfPeer("DGRO", "iShares Core Dividend Growth", "$29B", 29.0, "$210M", 210.0, "19.0x", 19.0, "4.0x", 4.0, "2.6x", 2.6, "+7%", 7.0, "21%", 21.0, "22%", 22.0, "+7%", 7.0, "2.4%", 2.4, "+7%", 7.0, "0.08%", 0.08, "$29B", 29.0, "0.09%", 0.09),
            etfPeer("JEPI", "JPMorgan Equity Premium", "$36B", 36.0, "$520M", 520.0, "20.0x", 20.0, "4.6x", 4.6, "2.9x", 2.9, "+5%", 5.0, "20%", 20.0, "20%", 20.0, "+4%", 4.0, "7.4%", 7.4, "0%", 0.0, "0.35%", 0.35, "$36B", 36.0, "0.15%", 0.15),
        ),
    )

    private val koreaIndexEtfPeers = PeerUniverse(
        label = "국내 상장 미국 S&P500 ETF",
        peers = listOf(
            etfPeer("TIGER 미국S&P500", "TIGER 미국S&P500", "4.1조원", 4.1, "842억원", 842.0, "24.0x", 24.0, "4.8x", 4.8, "3.1x", 3.1, "+7%", 7.0, "22%", 22.0, "24%", 24.0, "+8%", 8.0, "1.2%", 1.2, "+5%", 5.0, "0.07%", 0.07, "4.1조원", 4.1, "0.10%", 0.10),
            etfPeer("ACE 미국S&P500", "ACE 미국S&P500", "1.4조원", 1.4, "310억원", 310.0, "24.0x", 24.0, "4.8x", 4.8, "3.1x", 3.1, "+7%", 7.0, "22%", 22.0, "24%", 24.0, "+8%", 8.0, "1.1%", 1.1, "+5%", 5.0, "0.07%", 0.07, "1.4조원", 1.4, "0.11%", 0.11),
            etfPeer("KBSTAR 미국S&P500", "KBSTAR 미국S&P500", "0.9조원", 0.9, "180억원", 180.0, "24.0x", 24.0, "4.8x", 4.8, "3.1x", 3.1, "+7%", 7.0, "22%", 22.0, "24%", 24.0, "+8%", 8.0, "1.1%", 1.1, "+5%", 5.0, "0.07%", 0.07, "0.9조원", 0.9, "0.12%", 0.12),
            etfPeer("SOL 미국S&P500", "SOL 미국S&P500", "0.7조원", 0.7, "145억원", 145.0, "24.0x", 24.0, "4.8x", 4.8, "3.1x", 3.1, "+7%", 7.0, "22%", 22.0, "24%", 24.0, "+8%", 8.0, "1.0%", 1.0, "+5%", 5.0, "0.05%", 0.05, "0.7조원", 0.7, "0.12%", 0.12),
        ),
    )

    private val peerUniverses = mapOf(
        "NVDA" to aiDataCenterPeers,
        "005930" to memoryPeers,
        "OKLO" to smrPeers,
        "IONQ" to quantumPeers,
        "SCHD" to dividendEtfPeers,
        "TIGER 미국S&P500" to koreaIndexEtfPeers,
    )

    private fun pricePoints(vararg closes: Double): List<PricePoint> {
        return closes.mapIndexed { index, close ->
            PricePoint(
                label = "D${index + 1}",
                close = close,
            )
        }
    }

    private fun PeerUniverse.toComparisonSet(
        selectedSymbol: String,
        group: ComparisonMetricGroup,
    ): MetricComparisonSet {
        val orderedPeers = peers.sortedWith(
            compareByDescending<PeerProfile> { it.symbol == selectedSymbol }
                .thenBy { it.symbol },
        )
        val rows = metricDefinitions.getValue(group).map { definition ->
            MetricComparisonRow(
                metricName = definition.label,
                helper = definition.helper,
                higherIsBetter = definition.higherIsBetter,
                values = orderedPeers.map { peer ->
                    val metric = peer.metrics[definition.key]
                    MetricComparisonValue(
                        symbol = peer.symbol,
                        displayName = peer.name,
                        displayValue = metric?.displayValue ?: "-",
                        numericValue = metric?.numericValue,
                    )
                },
            )
        }
        return MetricComparisonSet(
            selectedSymbol = selectedSymbol,
            peerGroupLabel = label,
            group = group,
            rows = rows,
        )
    }

    private fun etfPeer(
        symbol: String,
        name: String,
        aumDisplay: String,
        aumValue: Double,
        tradingDisplay: String,
        tradingValue: Double,
        perDisplay: String,
        perValue: Double,
        pbrDisplay: String,
        pbrValue: Double,
        psrDisplay: String,
        psrValue: Double,
        revenueGrowthDisplay: String,
        revenueGrowthValue: Double,
        operatingMarginDisplay: String,
        operatingMarginValue: Double,
        roeDisplay: String,
        roeValue: Double,
        epsGrowthDisplay: String,
        epsGrowthValue: Double,
        dividendYieldDisplay: String,
        dividendYieldValue: Double,
        dividendGrowthDisplay: String,
        dividendGrowthValue: Double,
        expenseRatioDisplay: String,
        expenseRatioValue: Double,
        explicitAumDisplay: String,
        explicitAumValue: Double,
        trackingErrorDisplay: String,
        trackingErrorValue: Double,
    ): PeerProfile {
        return PeerProfile(
            symbol = symbol,
            name = name,
            metrics = metrics(
                "marketCap" to metric(aumDisplay, aumValue),
                "tradingValue" to metric(tradingDisplay, tradingValue),
                "per" to metric(perDisplay, perValue),
                "pbr" to metric(pbrDisplay, pbrValue),
                "psr" to metric(psrDisplay, psrValue),
                "revenueGrowth" to metric(revenueGrowthDisplay, revenueGrowthValue),
                "operatingMargin" to metric(operatingMarginDisplay, operatingMarginValue),
                "roe" to metric(roeDisplay, roeValue),
                "epsGrowth" to metric(epsGrowthDisplay, epsGrowthValue),
                "debtRatio" to metric("보유기업 평균", null),
                "currentRatio" to metric("보유기업 평균", null),
                "interestCoverage" to metric("보유기업 평균", null),
                "operatingCashFlow" to metric("포트폴리오", null),
                "freeCashFlow" to metric("포트폴리오", null),
                "fcfYield" to metric("4.2%", 4.2),
                "dividendYield" to metric(dividendYieldDisplay, dividendYieldValue),
                "dividendGrowth" to metric(dividendGrowthDisplay, dividendGrowthValue),
                "expenseRatio" to metric(expenseRatioDisplay, expenseRatioValue),
                "aum" to metric(explicitAumDisplay, explicitAumValue),
                "trackingError" to metric(trackingErrorDisplay, trackingErrorValue),
            ),
        )
    }

    private fun metrics(vararg values: Pair<String, MetricCell>): Map<String, MetricCell> {
        return mapOf(*values)
    }

    private fun metric(
        displayValue: String,
        numericValue: Double?,
    ) = MetricCell(displayValue, numericValue)

    private data class MetricDefinition(
        val key: String,
        val label: String,
        val helper: String,
        val higherIsBetter: Boolean,
    )

    private data class PeerUniverse(
        val label: String,
        val peers: List<PeerProfile>,
    )

    private data class PeerProfile(
        val symbol: String,
        val name: String,
        val metrics: Map<String, MetricCell>,
    )

    private data class MetricCell(
        val displayValue: String,
        val numericValue: Double?,
    )
}
