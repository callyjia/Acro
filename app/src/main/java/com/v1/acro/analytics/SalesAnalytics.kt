package com.v1.acro.analytics

import com.v1.acro.database.Transaction.TransactionData
import java.util.Calendar
import java.util.Locale

/**
 * ============================================================
 * SalesAnalytics.kt
 * ============================================================
 *
 * NEW FILE (this update). Pure (UI-free, DB-free) computation of sales statistics
 * from the list of orders. Fed by TransactionViewModel.allTransactions.
 *
 * Used by the Analytics screen and the Home dashboard sales card.
 * ============================================================
 */

/** Average / min / max revenue across a set of periods (days, weeks, or months). */
data class PeriodStats(
    val average: Double,
    val min: Double,
    val max: Double,
    val periods: Int   // number of distinct active periods that had at least one order
)

/** Full sales summary derived from all orders. */
data class SalesSummary(
    val totalRevenue: Double,
    val totalOrders: Int,
    val avgOrderValue: Double,
    val minOrderValue: Double,
    val maxOrderValue: Double,
    val todayRevenue: Double,
    val ordersToday: Int,
    val ordersThisWeek: Int,
    val ordersThisMonth: Int,
    val daily: PeriodStats,
    val weekly: PeriodStats,
    val monthly: PeriodStats
) {
    companion object {
        private val EMPTY_PERIOD = PeriodStats(0.0, 0.0, 0.0, 0)
        val EMPTY = SalesSummary(
            totalRevenue = 0.0,
            totalOrders = 0,
            avgOrderValue = 0.0,
            minOrderValue = 0.0,
            maxOrderValue = 0.0,
            todayRevenue = 0.0,
            ordersToday = 0,
            ordersThisWeek = 0,
            ordersThisMonth = 0,
            daily = EMPTY_PERIOD,
            weekly = EMPTY_PERIOD,
            monthly = EMPTY_PERIOD
        )
    }
}

private fun calendarFor(timestamp: Long): Calendar =
    Calendar.getInstance().apply { timeInMillis = timestamp }

// Period keys — two timestamps in the same day/week/month share a key.
private fun dayKey(ts: Long): Int =
    calendarFor(ts).let { it.get(Calendar.YEAR) * 1000 + it.get(Calendar.DAY_OF_YEAR) }

private fun weekKey(ts: Long): Int =
    calendarFor(ts).let { it.get(Calendar.YEAR) * 100 + it.get(Calendar.WEEK_OF_YEAR) }

private fun monthKey(ts: Long): Int =
    calendarFor(ts).let { it.get(Calendar.YEAR) * 100 + (it.get(Calendar.MONTH) + 1) }

private fun statsFromPeriodTotals(periodTotals: Collection<Double>): PeriodStats {
    if (periodTotals.isEmpty()) return PeriodStats(0.0, 0.0, 0.0, 0)
    return PeriodStats(
        average = periodTotals.average(),
        min = periodTotals.minOrNull() ?: 0.0,
        max = periodTotals.maxOrNull() ?: 0.0,
        periods = periodTotals.size
    )
}

/**
 * Compute the full sales summary.
 *
 * @param transactions all orders (any order)
 * @param now current time in millis (pass System.currentTimeMillis())
 */
fun computeSalesSummary(transactions: List<TransactionData>, now: Long): SalesSummary {
    if (transactions.isEmpty()) return SalesSummary.EMPTY

    val totals = transactions.map { it.total }
    val totalRevenue = totals.sum()
    val totalOrders = transactions.size

    // Revenue grouped per day / week / month
    val dayTotals = transactions.groupBy { dayKey(it.timestamp) }
        .mapValues { e -> e.value.sumOf { it.total } }
    val weekTotals = transactions.groupBy { weekKey(it.timestamp) }
        .mapValues { e -> e.value.sumOf { it.total } }
    val monthTotals = transactions.groupBy { monthKey(it.timestamp) }
        .mapValues { e -> e.value.sumOf { it.total } }

    val todayKey = dayKey(now)
    val weekNowKey = weekKey(now)
    val monthNowKey = monthKey(now)

    return SalesSummary(
        totalRevenue = totalRevenue,
        totalOrders = totalOrders,
        avgOrderValue = totalRevenue / totalOrders,
        minOrderValue = totals.minOrNull() ?: 0.0,
        maxOrderValue = totals.maxOrNull() ?: 0.0,
        todayRevenue = dayTotals[todayKey] ?: 0.0,
        ordersToday = transactions.count { dayKey(it.timestamp) == todayKey },
        ordersThisWeek = transactions.count { weekKey(it.timestamp) == weekNowKey },
        ordersThisMonth = transactions.count { monthKey(it.timestamp) == monthNowKey },
        daily = statsFromPeriodTotals(dayTotals.values),
        weekly = statsFromPeriodTotals(weekTotals.values),
        monthly = statsFromPeriodTotals(monthTotals.values)
    )
}

/** Format a money amount with thousands separators and 2 decimals, e.g. 1250.5 -> "1,250.50". */
fun formatMoney(value: Double): String = String.format(Locale.US, "%,.2f", value)
