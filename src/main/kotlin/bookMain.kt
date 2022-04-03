import java.io.File

fun main() {
    startProgramMenu()
}

fun startProgramMenu() {
    println("Press 1 to view all books\nPress 2 to add a book\nPress 3 to update a book\nPress 4 to delete a book\nPress 5 to view book with most pages\nPress 6 to view book with least pages\nPress 7 to view book with pages greater than or equal to 200\nPress 8 to view book with pages less than 200\nPress 9 to view book with pages between 50-300 inclusive\nPress 10 to Exit\n")
    val getMenuValue = readLine()
    if (getMenuValue != null && getMenuValue != "") {
        when (getMenuValue) {
            "1" -> {
                viewAllBooks()
            }
            "2" -> {
                addBookContext()
            }
            "3" -> {
                getUpdateTable()
            }
            "4" -> {
                setDeleteTable()
            }
            "5" -> {
                getBookWithPages(1)
                //view book with max pages
            }
            "6" -> {
                getBookWithPages(2)
                //view book with min pages
            }
            "7" -> {
                getBookWithPages(3)
                //view book with pages greater than or equal to 200
            }
            "8" -> {
                getBookWithPages(4)
                //view book with pages less than 200
            }
            "9" -> {
                getBookWithPages(5)
                //view book with pages between 50-300 inclusive
            }
            "10" -> {
                fileToList()
            }
            else -> main()
        }
    } else {
        main()
    }
}

fun setDeleteTable() {
    val myList = mutableListOf<String>()
    File(fileLocation()).useLines { lines -> myList.addAll(lines) }
    myList.forEachIndexed { i, line ->
        println(
            if (i <= 2) {
                "   $line"
            } else {
                "${i - 2}: " + line
            }
        )
    }
    println("Enter ISBN to delete")
    val userReturnValue = userReturnValue()
    if (userReturnValue != "") {
        deleteBookLine(myList, userReturnValue)
    } else {
        println("No data found for value")
        setDeleteTable()
    }
}

fun deleteBookLine(myList: MutableList<String>, lineNumberInput: String) {
    val deletedList = mutableListOf<String>()
    val checkList =mutableListOf<String>()
    myList.forEachIndexed { i, line ->
        if(i > 2){
            if (line.substring(60,73).trim() == lineNumberInput) {
                checkList.add(line.substring(60,73))
            } else {
//                checkList.add(line.substring(60,73))
                deletedList.add(line)
            }
        } else {
            deletedList.add(line)
        }
    }
    if(checkList[0].isNotEmpty()){
        if(checkList[0].trim() == lineNumberInput){
            val updatedString = covertMutableListToString(deletedList)
            File(fileLocation()).writeText(updatedString)
            println("File Deleted.")
            fileToList()
        } else {
            println("No data found to delete.")
        }
    } else {
        println("No data found")
    }
}

fun getUpdateTable() {
    val myList = mutableListOf<String>()
    File(fileLocation()).useLines { lines -> myList.addAll(lines) }
    myList.forEachIndexed { i, line ->
        println(
            if (i <= 2) {
                "   $line"
            } else {
                "${i - 2}: " + line
            }
        )
    }
    println("Enter ISBN to update")
    val userReturnValue = userReturnValue()
    if (userReturnValue != "") {
        updateBookLine(myList, userReturnValue)
    } else {
        println("No data found for value")
    }
}

fun updateBookLine(myList: MutableList<String>, ISBNNumberInput: String) {
    val valuesOfPages = mutableListOf<String>()
    myList.forEachIndexed { i, line ->
        if (i > 2) {
            val convert = line.substring(60,73).trim()
            valuesOfPages.add(convert)
        }
    }
    myList.forEachIndexed { i, line ->
        if(i>2){
            if (line.substring(60,73).trim() == ISBNNumberInput) {
                println(line)
            }
        }
    }
    val title = addTitle()
    val author = addAuthor()
    val publicationYear = addPublicationYear()
    val numberOfPages = addNumberPages()
    val isbn = addISBN()
    if (publicationYear > 1600) {
        if (numberOfPages > 1) {
            if (title != "" && author != "" && publicationYear != 0 && numberOfPages != 0 && isbn != "") {
                val extraSpaceISBN = sliceStringForBook(isbn, 13).padEnd(14)
                updateBook(
                    sliceStringForBook(title, 30).padEnd(31),
                    sliceStringForBook(author, 15).padEnd(16),
                    sliceStringForBook(publicationYear.toString(), 4).padStart(6),
                    sliceStringForBook(numberOfPages.toString(), 5).padStart(6),
                    extraSpaceISBN.padStart(15),
                    ISBNNumberInput,
                    myList
                )
            } else {
                println("Please provide all valid values.")
                updateBookLine(myList, ISBNNumberInput)
            }
        } else {
            println("Number of pages should be greater then 1.")
            getUpdateTable()
        }
    } else {
        println("Publication year can not be less then 1600.")
        getUpdateTable()
    }
}

fun updateBook(
    title: String,
    author: String,
    publicationYear: String,
    numberOfPages: String,
    ISBN: String,
    ISBNNumberInput: String,
    myList: MutableList<String>
) {
    val addBooksToList = mutableListOf<String>()
    val checkTable = mutableListOf<Int>()
    myList.forEachIndexed { i, getLine ->
        if(i > 2){
            if (getLine.substring(60,73).trim() == ISBNNumberInput) {
                val updateLine =
                    title + author + publicationYear + numberOfPages + ISBN + "https://www.biblio.com/" + ISBN.trimStart()
                        .trimEnd() + " (Links to an external site.)"
                addBooksToList.add(updateLine)
                checkTable.add(i)
            } else {
                addBooksToList.add(getLine)
                checkTable.add(i)
            }
        } else {
            addBooksToList.add(getLine)
            checkTable.add(i)
        }
    }

    val updatedString = covertMutableListToString(addBooksToList)
    if (checkTable.size > 2) {
        File(fileLocation()).writeText(updatedString)
        println("File Updated.")
    }
    fileToList()
}

fun userReturnValue(): String {
    return try {
        return readLine().toString()
    } catch (ex: Exception) {
        println("Please provide number value for line.\n")
        userReturnValue()
    }
}

fun addBookContext() {
    val title = addTitle()
    val author = addAuthor()
    val publicationYear = addPublicationYear()
    val numberOfPages = addNumberPages()
    val isbn = addISBN()
    if (publicationYear > 1600) {
        if (numberOfPages > 1) {
            if (title != "" && author != "" && publicationYear != 0 && numberOfPages != 0 && isbn != "") {
                val extraSpaceISBN = sliceStringForBook(isbn, 13).padEnd(14)
                addBook(
                    sliceStringForBook(title, 30).padEnd(31),
                    sliceStringForBook(author, 15).padEnd(16),
                    sliceStringForBook(publicationYear.toString(), 4).padStart(6),
                    sliceStringForBook(numberOfPages.toString(), 5).padStart(6),
                    extraSpaceISBN.padStart(15)
                )
            } else {
                println("Please provide all values.")
                addBookContext()
            }
        } else {
            println("Number of pages should be greater then 1.")
            addBookContext()
        }
    } else {
        println("Publication year can not be less then 1600.")
        addBookContext()
    }
}

fun sliceStringForBook(title: String, i: Int): String {
    return title.chunked(i)[0]
}

fun addTitle(): String {
    println("Add a TITLE: \n")
    return readLine().toString()
}

fun addAuthor(): String {
    println("Add a AUTHOR: \n")
    return readLine().toString()
}

fun addPublicationYear(): Int {
    println("Add PUBLICATION YEAR: \n")
    return try {
        readLine()?.toInt() ?: 0
    } catch (ex: NumberFormatException) {
        println("please provide year only for PUBLICATION YEAR.\n")
        addPublicationYear()
    }
}

fun addNumberPages(): Int {
    println("Add NUMBER OF PAGES : \n")
    return try {
        readLine()?.toInt() ?: 0
    } catch (ex: NumberFormatException) {
        println("please provide number only for PAGES.\n")
        addNumberPages()
    }
}

fun addISBN(): String {
    println("Add a ISBN: \n")
    return readLine().toString()
}
fun getBookWithPages(pages: Int) {
    val myList = mutableListOf<String>()
    File(fileLocation()).useLines { lines -> myList.addAll(lines) }

    val valuesOfPages = mutableListOf<Int>()
    myList.forEachIndexed { i, line ->
        if (i > 2) {
            val convert = line.substring(54,59).trim()
            valuesOfPages.add(convert.toInt())
        }
    }
    when (pages) {
        1 -> {
            println("Maximum number of pages in book:\n")
            val max: Int = valuesOfPages.maxOrNull() ?: 0
            getBookWithValueMinMax(myList,  max,0, pages)
        }
        2 -> {
            println("Minimum number of pages in book:\n")
            val min: Int = valuesOfPages.minOrNull() ?: 0
            getBookWithValueMinMax(myList, min,0, pages)
        }
        3 -> {
            println("Books with pages greater than or equal to 200:\n")
            getBookWithValueMinMax(myList,200,0,  pages)
        }
        4 -> {
            println("Books with pages less than 200:\n")
            getBookWithValueMinMax(myList, 200,0,  pages)
        }
        5 -> {
            println("Books with pages less than 200:\n")
            getBookWithValueMinMax(myList, 50,300,  pages)
        }
    }
}

fun getBookWithValueMinMax(myList: MutableList<String>, value: Int, value2: Int, compareValue: Int) {
    when (compareValue) {
        1 -> {
            myList.forEachIndexed { i, line ->
                if (i > 2) {
                    if (line.chunked(5)[11].trim().toInt() == value) {

                        println("${i - 2}: " + line)
                    }
                } else if (i < 2) {
                    println("   $line")
                }
            }
        }
        2 -> {
            myList.forEachIndexed { i, line ->
                if (i > 2) {
                    if (line.chunked(5)[11].trim().toInt() == value) {
                        println("${i - 2}: " + line)
                    }
                } else if (i < 2) {
                    println("   $line")
                }
            }
        }
        3 -> {
            myList.forEachIndexed { i, line ->
                if (i > 2) {
                    if (line.chunked(5)[11].trim().toInt() >= value) {
                        println("${i - 2}: " + line)
                    }
                } else if (i < 2) {
                    println("   $line")
                }
            }
        }
        4 -> {
            myList.forEachIndexed { i, line ->
                if (i > 2) {
                    if (line.chunked(5)[11].trim().toInt() < value) {
                        println("${i - 2}: " + line)
                    }
                } else if (i < 2) {
                    println("   $line")
                }
            }
        }
        5 -> {
            myList.forEachIndexed { i, line ->
                if (i > 2) {
                    if (line.chunked(5)[11].trim().toInt() in value..value2) {
                        println("${i - 2}: " + line)
                    }
                } else if (i < 2) {
                    println("   $line")
                }
            }
        }
    }
    askExit()
}
fun addBook(title: String, author: String, publicationYear: String, numberOfPages: String, ISBN: String) {
    File(fileLocation()).writeText(
        toString() + "\n" + title + author + publicationYear + numberOfPages + ISBN + "https://www.biblio.com/" + ISBN.trimStart()
            .trimEnd() + " (Links to an external site.)"
    )
    println("File Updated.")
    fileToList()
}

fun viewAllBooks() {
    val myList = mutableListOf<String>()

    File(fileLocation()).useLines { lines -> myList.addAll(lines) }
    myList.forEachIndexed { _, line -> println(line) }
    askExit()
}

fun covertMutableListToString(toList: MutableList<String>): String {
    return toList.joinToString("\n")
}

fun fileToList() {
    val myList = mutableListOf<String>()

    File(fileLocation()).useLines { lines -> myList.addAll(lines) }
    myList.forEachIndexed { _, line -> println(line) }
    askExit()
}

fun fileLocation(): String {
    return "src/main/resources/books.txt"
}

fun toString(): String {
    return File(fileLocation()).readText()
}

fun askExit(){
    println("Do you want to run the program again? Reply with 1 if YES 2 if NO.")
    when (readLine()?.toInt()) {
        1 -> {
            main()
        }
        2 -> {
            println("Exit.")
        }
        else -> {
        }
    }
}

/*Changing the text file manually will cause program to run correctly */
/*Please use this table structure*/
/*
Geeks Publishing, Inc.
Name                           Author          Pub Yr Pages ISBN          URL
------------------------------ --------------- ------ ----- ------------- -------
Assimilating RubyMine          Dave Jones        2013    66 1849698767    https://www.biblio.com/1849698767 (Links to an external site.)
Kotlin Programming: The Big Ne Josh Skeen        2018   384 0135161630    https://www.biblio.com/0135161630 (Links to an external site.)
Kotlin In-Depth [Vol-I]: A Com Aleksei Sedunov   2020   338 9389328586    https://www.biblio.com/9389328586 (Links to an external site.)
Kotlin in Action               Dmitry Jemerov    2017   360 1617293296    https://www.biblio.com/1617293296 (Links to an external site.)
Learning React Native: Buildin Bonnie Eisenman   2017   242 1491989149    https://www.biblio.com/1491989149 (Links to an external site.)
*/