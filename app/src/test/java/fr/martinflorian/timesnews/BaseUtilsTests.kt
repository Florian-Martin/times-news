package fr.martinflorian.timesnews

import fr.martinflorian.timesnews.utils.capitalizeAllFirstChars
import fr.martinflorian.timesnews.utils.capitalizeFirstChar
import fr.martinflorian.timesnews.utils.parseDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class BaseUtilsTests {

    @Test
    fun capitalizeFirstCharTest() {
        assertEquals("Hello", "hello".capitalizeFirstChar())
        assertNotEquals("hello", "Hello".capitalizeFirstChar())
    }

    @Test
    fun capitalizeAllFirstCharsTest() {
        assertEquals("By Jean Michel, Jack Sparrow, Luke Skywalker", "by JEAN mICHEL, jack sparrow, luKE skywalker".capitalizeAllFirstChars())
        assertNotEquals("hello world", "Hello world".capitalizeFirstChar())
    }

    @Test
    fun parseDateTest() {
        assertEquals("2022-12-15 03:00:09", "2022-12-15T03:00:09-05:00".parseDate())
    }
}