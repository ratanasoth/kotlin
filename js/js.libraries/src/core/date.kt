package kotlin.js

/**
 * Exposes the [Date API](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date) to Kotlin.
 */
public external class Date() {
    public constructor(milliseconds: Number)

    public constructor(dateString: String)

    public constructor(year: Int, month: Int)

    public constructor(year: Int, month: Int, day: Int)

    public constructor(year: Int, month: Int, day: Int, hour: Int)

    public constructor(year: Int, month: Int, day: Int, hour: Int, minute: Int)

    public constructor(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int)

    public constructor(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Number)

    public fun getDate(): Int

    public fun setDate(dayValue: Int): Unit

    public fun getDay(): Int

    public fun getFullYear(): Int

    public fun setFullYear(yearValue: Int): Unit

    public fun setFullYear(yearValue: Int, monthValue: Int): Unit

    public fun setFullYear(yearValue: Int, monthValue: Int, dayValue: Int): Unit

    public fun getHours(): Int

    public fun setHours(hoursValue: Int): Unit

    public fun setHours(hoursValue: Int, minutesValue: Int): Unit

    public fun setHours(hoursValue: Int, minutesValue: Int, secondsValue: Int): Unit

    public fun setHours(hoursValue: Int, minutesValue: Int, secondsValue: Int, msValue: Int): Unit

    public fun getMilliseconds(): Int

    public fun setMilliseconds(millisecondsValue: Int): Unit

    public fun getMinutes(): Int

    public fun setMinutes(minutesValue: Int): Unit

    public fun setMinutes(minutesValue: Int, secondsValue: Int): Unit

    public fun setMinutes(minutesValue: Int, secondsValue: Int, msValue: Int): Unit

    public fun getMonth(): Int

    public fun setMonth(monthValue: Int): Unit

    public fun setMonth(monthValue: Int, dayValue: Int): Unit

    public fun getSeconds(): Int

    public fun setSeconds(secondsValue: Int): Unit

    public fun setSeconds(secondsValue: Int, msValue: Int): Unit

    public fun getTime(): Number

    public fun setTime(timeValue: Number): Unit

    public fun getTimezoneOffset(): Int

    public fun getUTCDate(): Int

    public fun setUTCDate(dayValue: Int): Unit

    public fun getUTCDay(): Int

    public fun getUTCFullYear(): Int

    public fun setUTCFullYear(yearValue: Int): Unit

    public fun setUTCFullYear(yearValue: Int, monthValue: Int): Unit

    public fun setUTCFullYear(yearValue: Int, monthValue: Int, dayValue: Int): Unit

    public fun getUTCHours(): Int

    public fun setUTCHours(hoursValue: Int): Unit

    public fun setUTCHours(hoursValue: Int, minutesValue: Int): Unit

    public fun setUTCHours(hoursValue: Int, minutesValue: Int, secondsValue: Int): Unit

    public fun setUTCHours(hoursValue: Int, minutesValue: Int, secondsValue: Int, msValue: Int): Unit

    public fun getUTCMilliseconds(): Int

    public fun setUTCMilliseconds(millisecondsValue: Int): Unit

    public fun getUTCMinutes(): Int

    public fun setUTCMinutes(minutesValue: Int): Unit

    public fun setUTCMinutes(minutesValue: Int, secondsValue: Int): Unit

    public fun setUTCMinutes(minutesValue: Int, secondsValue: Int, msValue: Int): Unit

    public fun getUTCMonth(): Int

    public fun setUTCMonth(monthValue: Int): Unit

    public fun setUTCMonth(monthValue: Int, dayValue: Int): Unit

    public fun getUTCSeconds(): Int

    public fun setUTCSeconds(secondsValue: Int): Unit

    public fun setUTCSeconds(secondsValue: Int, msValue: Int): Unit

    public fun toDateString(): String

    public fun toISOString(): String

    public fun toJSON(): Json

    public fun toLocaleDateString(locales: Array<String>? = definedExternally, options: Json? = definedExternally): String

    public fun toLocaleDateString(locales: String): String

    public fun toLocaleDateString(locales: String, options: Json): String

    public fun toLocaleDateString(locales: Array<String>? = definedExternally, options: LocaleOptions?): String

    public fun toLocaleDateString(locales: String, options: LocaleOptions?): String

    public fun toLocaleString(locales: Array<String>? = definedExternally, options: Json? = definedExternally): String

    public fun toLocaleString(locales: String): String

    public fun toLocaleString(locales: String, options: Json): String

    public fun toLocaleString(locales: Array<String>? = definedExternally, options: LocaleOptions?): String

    public fun toLocaleString(locales: String, options: LocaleOptions?): String

    public fun toLocaleTimeString(locales: Array<String>? = definedExternally, options: Json? = definedExternally): String

    public fun toLocaleTimeString(locales: String): String

    public fun toLocaleTimeString(locales: String, options: Json): String

    public fun toLocaleTimeString(locales: Array<String>? = definedExternally, options: LocaleOptions?): String

    public fun toLocaleTimeString(locales: String, options: LocaleOptions?): String

    public fun toTimeString(): String

    public fun toUTCString(): String

    public companion object {
        public fun now(): Number

        public fun parse(dateString: String): Number

        public fun UTC(year: Int, month: Int): Number

        public fun UTC(year: Int, month: Int, day: Int): Number

        public fun UTC(year: Int, month: Int, day: Int, hour: Int): Number

        public fun UTC(year: Int, month: Int, day: Int, hour: Int, minute: Int): Number

        public fun UTC(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Number

        public fun UTC(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Number): Number
    }

    public interface LocaleOptions {
        public var localeMatcher: String?

        public var timeZone: String?

        public var hour12: Boolean?

        public var weekday: String?

        public var era: String?

        public var year: String?

        public var month: String?

        public var day: String?

        public var hour: String?

        public var minute: String?

        public var second: String?

        public var timeZoneName: String?
    }
}

public inline fun dateLocaleOptions(init: Date.LocaleOptions.() -> Unit): Date.LocaleOptions {
    val result = js("new Object()").unsafeCast<Date.LocaleOptions>()
    init(result)
    return result
}