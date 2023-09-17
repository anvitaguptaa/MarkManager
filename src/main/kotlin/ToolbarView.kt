import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.collections.*
import java.util.*
import javafx.scene.layout.Pane
import kotlin.collections.ArrayList

val sortOptions = listOf("Course Code", "Term", "Grade (Ascending)", "Grade (Descending)")
val filterOptions = listOf("None", "CS Courses", "MATH Courses", "Non-CS/MATH Courses")

class ToolbarView (val viewModel: Model) : VBox(), IView {
    //  TOP TOOLBAR
    val topToolbar = ToolBar()
    val sortLabel = Label("Sort By:   ")
    val sortChoice = ComboBox(FXCollections.observableList(sortOptions))
    val sortSeperator = Separator(Orientation.VERTICAL)
    val filterLabel = Label("Filter By:   ")
    val filterChoice = ComboBox(FXCollections.observableList(filterOptions))
    val filterSeperator = Separator(Orientation.VERTICAL)
    val wdCheckBox = CheckBox("Include WD")
    var spacerThree = Pane()
    var spacerFour = Region()
    var darkMode = CheckBox("Dark Mode")
//    var testButton = Button()

    //  BOTTOM TOOLBAR
    val bottomToolbar = ToolBar()
    var courseCode = TextField()
    var courseName = TextField("Course Name")
    val termOptions = listOf("F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23", "S23", "F23")
    var term = ComboBox(FXCollections.observableList(termOptions))
    var grade = TextField()
    val createButton = Button("Create")
    val spacer = Region()
    val spacerTwo = Region()
    val holder = HBox()

    // Filters values in course array
    fun filterArray(str: String): ArrayList<Course> {
        val array = ArrayList<Course>()
        for (course in viewModel.courses) {
            val code = course.courseCode.toUpperCase()
            if (str == "CS") {
                if (code.startsWith(str)) {
                    array.add(course)
                }
            } else if (str == "MATH") {
                if (code.startsWith(str) ||
                    code.startsWith("CO") ||
                    code.startsWith("STAT")) {
                    array.add(course)
                }
            } else if (str == "Non") {
                if (!(code.startsWith("MATH") ||
                            code.startsWith("CO") ||
                            code.startsWith("STAT") ||
                            code.startsWith("CS"))) {
                    array.add(course)
                }
            }
        }
        return array
    }

    init {
        var initCourses = viewModel.courses
        var filteredArray: ArrayList<Course>

        createButton.onAction = EventHandler {
            var course = Course(courseCode.text, courseName.text, term.value, grade.text)
            viewModel.courses += course

            for (item in viewModel.courses) {
                println(item.courseCode + " " + item.courseName + " " + item.term + " " + item.grade)
            }

            viewModel.update()
        }

        // Sorts values in course array
        sortChoice.onAction = EventHandler {
            for (course in viewModel.courses) {
                if (course.grade == "WD") {
                    course.grade = "-1"
                }
            }

            when (sortChoice.value) {
                "Grade (Ascending)" -> viewModel.courses.sortBy { it.grade.toInt() }
                "Grade (Descending)" -> viewModel.courses.sortByDescending { it.grade.toInt() }
                "Course Code" -> viewModel.courses.sortBy { it.courseCode }
                "Term" -> viewModel.courses.sortBy { it.term }
            }

            for (course in viewModel.courses) {
                if (course.grade == "-1") {
                    course.grade = "WD"
                }
            }
            viewModel.update()
        }

        // Filters values in course array
        filterChoice.onAction = EventHandler {
            filteredArray = if (filterChoice.value == "CS Courses") {
                filterArray("CS")
            } else if (filterChoice.value == "MATH Courses") {
                filterArray("MATH")
            } else if (filterChoice.value == "Non-CS/MATH Courses") {
                filterArray("Non")
            } else {
                initCourses
            }
            viewModel.courses = filteredArray
            viewModel.update()
            viewModel.courses = initCourses
        }

        // Checks if WD Included checkbox is clicked
        wdCheckBox.onAction = EventHandler {
            if (wdCheckBox.isSelected) {
                viewModel.isWDClicked = true
                viewModel.update()
//                println("CHECKBOXED CHECKED !!!!")
            } else {
                viewModel.isWDClicked = false
                viewModel.update()
            }
        }

        // Checks if darkmode checkbox is checked
        darkMode.onAction = EventHandler {
            if (darkMode.isSelected) {
                viewModel.isDarkModeClicked = true
                viewModel.update()
            } else {
                viewModel.isDarkModeClicked = false
                viewModel.update()
            }
        }

        topToolbar.items.addAll(
            sortLabel, sortChoice, sortSeperator, filterLabel, filterChoice, filterSeperator,
            wdCheckBox, spacerThree, darkMode, spacerFour
        )
        holder.children.addAll(
            spacer, courseCode, courseName, term, grade, createButton, spacerTwo
        )

        // Styling
        sortChoice.prefWidthProperty().bind(Bindings.divide(topToolbar.widthProperty(), 5))
        filterChoice.prefWidthProperty().bind(Bindings.divide(topToolbar.widthProperty(), 5))
        wdCheckBox.prefWidthProperty().bind(Bindings.divide(topToolbar.widthProperty(), 5))
        courseCode.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 5))
        courseName.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 5))
        term.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 5))
        grade.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 5))
        HBox.setHgrow(topToolbar, Priority.ALWAYS)
        HBox.setHgrow(holder, Priority.ALWAYS)
        HBox.setHgrow(spacerThree, Priority.ALWAYS)
        bottomToolbar.padding = Insets(10.0, 10.0, 10.0, 10.0)
        holder.style = "-fx-background-color : #cfcfcf; -fx-background-radius: 10 10 10 10;"
        holder.padding = Insets(10.0, 5.0, 10.0, 5.0)
        holder.spacing = 12.0
        bottomToolbar.items.add(holder)

        this.children.addAll(topToolbar, bottomToolbar)
        viewModel.addViewModel(this)
    }

    override fun update() {
        courseName.text = "Course Name"
        courseCode.clear()
        term.valueProperty().set(null)
        grade.clear()

        // Implements darkmode
        if (viewModel.isDarkModeClicked == true) {
            topToolbar.style = "-fx-background-color: gray; -fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: dimgrey;"
            bottomToolbar.style = "-fx-background-color: gray; -fx-border-style: solid; -fx-border-color: dimgrey;"
        } else {
            topToolbar.style = ""
            bottomToolbar.style = ""
        }
    }
}