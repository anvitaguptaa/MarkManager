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
import javafx.application.Platform

class StatusbarView (val viewModel: Model) : ToolBar(), IView {
    val courseAverage = Label("Course Average: ${viewModel.courseAverage}")
    val averageSeparator = Separator(Orientation.VERTICAL)
    val courseCount = Label("Courses Taken: ${viewModel.coursesTaken}")
    val coursesSeparator = Separator(Orientation.VERTICAL)
    val coursesFailed = Label("Courses Failed: ${viewModel.coursesFailed}")
    val coursesSeparatorTwo = Separator(Orientation.VERTICAL)
    val coursesWD = Label("Courses WD'ed: ${viewModel.coursesWD}")
    var spacer = Pane()
    var spacerTwo = Region()
    var exitButton = Button("Exit")

    init {
        coursesSeparatorTwo.isVisible = false
        coursesWD.isVisible = false
        HBox.setHgrow(spacer, Priority.ALWAYS)

        exitButton.onAction = EventHandler {
            Platform.exit()
        }

        this.items.addAll(
            courseAverage, averageSeparator, courseCount, coursesSeparator, coursesFailed,
            coursesSeparatorTwo, coursesWD, spacer, exitButton, spacerTwo
        )

        viewModel.addViewModel(this)
    }

    override fun update() {
        courseAverage.text = "Course Average: ${viewModel.courseAverage}"
        courseCount.text = "Courses Taken: ${viewModel.coursesTaken}"
        coursesFailed.text = "Courses Failed: ${viewModel.coursesFailed}"
        coursesWD.text = "Courses WD'ed: ${viewModel.coursesWD}"

        coursesSeparatorTwo.isVisible = viewModel.isWDClicked
        coursesWD.isVisible = viewModel.isWDClicked

        if (viewModel.isDarkModeClicked == true) {
            this.style = "-fx-background-color: gray; -fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: dimgrey;"

        } else {
            this.style = ""
        }
    }
}