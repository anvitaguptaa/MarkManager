import Model
import CoursesView
import ToolbarView
import StatusbarView
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage


class Main : Application() {
    override fun start(stage: Stage) {
        val model = Model()
        val root = BorderPane()

        root.top = ToolbarView(model)
        root.bottom = StatusbarView(model)
        root.center = CoursesView(model)
        stage.scene = Scene(root, 800.0, 600.0)
        stage.title = "Mark Manager"

        stage.show()
    }
}