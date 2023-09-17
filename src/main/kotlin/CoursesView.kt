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

// Returns the color to be used for each coursebar
fun getColor(grade: String): String {
    if (grade == "WD") {
        return "darkslategray"
    }
    var gradeInt = grade.toInt()
    return if (gradeInt < 50) {
        "lightcoral"
    } else if (gradeInt in 50..59) {
        "lightblue"
    } else if (gradeInt in 60..90) {
        "lightgreen"
    } else if (gradeInt in 91..95) {
        "silver"
    } else {
        "gold"
    }
}

// Creates a scrollpane to display added courses
class CoursesView (val viewModel: Model) : ScrollPane(), IView {
    init {
        viewModel.addViewModel(this)
    }

    // Creates a bar holding course information from toolbar input
    fun createCourseBar(course: Course): ToolBar {
        val initName = course.courseName
        val initGrade = course.grade
        val initTerm = course.term
        val courseBar = ToolBar()
        val courseCode = TextField(course.courseCode)
        courseCode.isEditable = false
        val courseName = TextField(initName)
        val termOptions = listOf("F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23", "S23", "F23")
        var term = ComboBox(FXCollections.observableList(termOptions))
        term.value = initTerm
        val grade = TextField(initGrade)
        val spacer = Region()
        val deleteButton = Button("Delete")
        val updateButton = Button("Update")
        updateButton.isDisable = true
        var colorString = "-fx-background-color : ${getColor(course.grade)}; -fx-background-radius: 10 10 10 10;"
        val undoButton = Button("Undo")
        undoButton.isVisible = false
        val holder = HBox()

        holder.children.addAll(
            spacer, courseCode, courseName, term, grade, updateButton, deleteButton, undoButton
        )

        // Deletes a course from the view
        deleteButton.onAction = EventHandler {
            println("COURSE REMOVED")
            viewModel.courses.remove(course)
            viewModel.update()
        }

        // Checks for updated coursename value
        courseName.textProperty().addListener { _, _, _ ->
            updateButton.isDisable = false

            course.courseName = courseName.text

            holder.children.remove(deleteButton)
            undoButton.isVisible = true
        }

        // Checks for updated term value
        term.onAction = EventHandler {
            updateButton.isDisable = false
            course.term = term.value

            holder.children.remove(deleteButton)
            undoButton.isVisible = true
        }

        // Checks for updated grade value
        grade.textProperty().addListener { _, _, newValue ->
            updateButton.isDisable = false
            course.grade = newValue

            holder.children.remove(deleteButton)
            undoButton.isVisible = true
            colorString = "-fx-background-color : ${getColor(newValue)}; -fx-background-radius: 10 10 10 10;"
        }

        // Checks for update
        updateButton.onMouseClicked = EventHandler {
            holder.style = colorString
            courseName.editableProperty().set(false)
            grade.editableProperty().set(false)

            term.disableProperty().set(true)
            term.style = "-fx-opacity: 3; -fx-text-fill: black;-fx-background-color: white"

            updateButton.isDisable = true
            holder.children.remove(undoButton)
            holder.children.add(deleteButton)

        }

        // Implements undo after update
        undoButton.onAction = EventHandler {
            updateButton.isDisable = true

            courseName.text = initName
            grade.text = initGrade
            term.value = initTerm

            colorString = "-fx-background-color : ${getColor(grade.text)}; -fx-background-radius: 10 10 10 10;"
            holder.style = colorString

            holder.children.remove(undoButton)
            holder.children.addAll(deleteButton, undoButton)
            undoButton.isVisible = false
        }

        // Styling
        courseCode.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 6))
        courseName.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 6))
        term.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 6))
        grade.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 6))
        courseBar.padding = Insets(10.0, 10.0, 10.0, 10.0)
        holder.style = colorString
        holder.padding = Insets(10.0, 5.0, 10.0, 5.0)
        holder.spacing = 12.0
        courseBar.items.add(holder)
        HBox.setHgrow(courseBar, Priority.ALWAYS)
        HBox.setHgrow(holder, Priority.ALWAYS)

        return courseBar

    }
    override fun update() {
        var coursesBox = VBox()
        var courses = viewModel.courses

        // Iterates through courses array to create courses display in scrollpane
        for (course in courses) {
            println("${course.courseCode} ${course.courseName} ${course.term} ${course.grade}")
            if (!viewModel.isWDClicked) {
                if (course.grade != "WD") {
                    val courseBar = createCourseBar(course)
                    coursesBox.children.add(courseBar)
                }
            } else {
                val courseBar = createCourseBar(course)
                coursesBox.children.add(courseBar)
            }
        }

        coursesBox.prefWidthProperty().bind(this.widthProperty())
        this.content = coursesBox
    }
}