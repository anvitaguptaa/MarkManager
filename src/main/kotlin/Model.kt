import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt

// View calls model and model calls views to rerender all of it
class Model {
    var courses: ArrayList<Course> = ArrayList()
    var viewModels = ArrayList<IView>()

    var courseAverage: Double = 0.0
    var coursesTaken: Int = 0
    var coursesFailed: Int = 0
    var coursesWD: Int = 0
    var isWDClicked: Boolean = false
    var isDarkModeClicked: Boolean = false


    fun addViewModel(viewModel: IView) {
        viewModels.add(viewModel)
    }

    // Gets the amount of courses taken
    fun getNumCoursesTaken(): Int {
        var numCourses = 0

        for (course in courses) {
            numCourses++
            print(numCourses)
        }

        println("NUM COURSES: " + numCourses)
        return numCourses
    }


    // iterate through courses in model to calcultae statusbar
    fun calcAverage(): Double {
        var numCourses = 0
        var avg: Double = 0.0

        if (courses.isEmpty()) {
            return 0.0
        }

        for (course in courses) {
            if (course.grade == "WD") {
                avg += 0.0
            } else {
                var gradeNum = course.grade.toDouble()
                avg += gradeNum
                numCourses++
            }
        }

        if (avg == 0.0) {
            return 0.0
        }

        return (avg / numCourses)
    }

    // Calculates the grade average from courses "taken"
    fun getFailedCourses(): Int {
        var failedCourses = 0

        for (course in courses) {
            if (course.grade != "WD") {
                var grade = course.grade.toInt()
                if (grade < 50) {
                    failedCourses++
                }
            }
        }
        return failedCourses
    }

    // Calculates the number of courses taken
    fun getNumCoursesWD(): Int {
        var coursesWD = 0

        for (course in courses) {
            if (course.grade == "WD") {
                coursesWD++
            }
        }
        return coursesWD
    }

    fun update() {
        courseAverage = (calcAverage() * 100.0).roundToInt() / 100.0
        coursesTaken = getNumCoursesTaken()
        coursesFailed = getFailedCourses()
        coursesWD = getNumCoursesWD()

        for (viewModel in viewModels) {
            viewModel.update()
        }
    }
}