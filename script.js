const urlParams = new URLSearchParams(window.location.search);
const semester = urlParams.get("semester");

console.log("Selected semester:", semester);


// ===============================
// SHOW STUDY MATERIAL
// ===============================

function showMaterials(subject) {
    const materialSection = document.getElementById("material-section");
    materialSection.style.display = "block";
    materialSection.innerHTML = `
    <h2>📚 ${subject} Study Material</h2>

    <div class="material-list">

        <div class="material-item">
            📄 <a href="materials/Semiconductor/semiconductor.pdf" target="_blank">
                semiconductor.pdf
            </a>
        </div>

        <div class="material-item">
            📝 <a href="#" onclick="return false;">
                Previous Year Papers
            </a>
        </div>

        <div class="material-item">
            📋 <a href="#" onclick="return false;">
                Important Questions
            </a>
        </div>

    </div>
`;
}


// ===============================
// SEMESTER TITLE
// ===============================

const semesterTitle = document.getElementById("semester-title");

if (semester) {
    semesterTitle.textContent = "Semester " + semester;
}


// ===============================
// SUBJECTS FOR ALL 8 SEMESTERS
// ===============================

const subjects = {

    1: [
        "Semiconductor",
        "Programming in C",
        "Mathematics 1",
        "IDS"
    ],

    2: [
        "Data Structures",
        "Digital Electronics",
        "Mathematics 2",
        "Object Oriented Programming"
    ],

    3: [
        "Database Management System",
        "Operating System",
        "Computer Networks",
        "Discrete Mathematics"
    ],

    4: [
        "Software Engineering",
        "Web Technology",
        "Computer Architecture",
        "Theory of Computation"
    ],

    5: [
        "Artificial Intelligence",
        "Machine Learning",
        "Compiler Design",
        "Cloud Computing"
    ],

    6: [
        "Cyber Security",
        "Distributed Systems",
        "Data Mining",
        "Mobile Computing"
    ],

    7: [
        "Big Data",
        "Internet of Things",
        "Blockchain",
        "Elective"
    ],

    8: [
        "Project",
        "Internship",
        "Major Project",
        "Elective"
    ]

};


// ===============================
// DISPLAY SUBJECTS
// ===============================

const subjectsContainer =
    document.getElementById("subjects-container");

if (semester && subjects[semester]) {

    subjectsContainer.innerHTML = "";

    subjects[semester].forEach(function(subject) {

        subjectsContainer.innerHTML += `
    <div class="subject-card" onclick="showMaterials('${subject}')">
        <h2>${subject}</h2>
        <p>Notes, PYQs and study material.</p>
        <span>Explore →</span>
    </div>
`;

    });

}