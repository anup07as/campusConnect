const urlParams = new URLSearchParams(window.location.search);
const semester = urlParams.get("semester");

console.log("Selected semester:", semester);


// ===============================
// SHOW STUDY MATERIAL
// ===============================
function showMaterials(subject, subjectId) {

    const materialSection =
        document.getElementById("material-section");

    materialSection.style.display = "block";

    materialSection.innerHTML = `
    <h2>📚 ${subject} Study Material</h2>
    <div class="material-list">
        <p>Loading resources...</p>
    </div>
`;

fetch("http://127.0.0.1:8080/api/resources?subjectId=" + subjectId)
    .then(response => response.json())
    .then(resources => {

        const materialList =
            materialSection.querySelector(".material-list");

        materialList.innerHTML = "";

        resources.forEach(function(resource) {

            materialList.innerHTML += `
                <div class="material-item">
                    📄
                    <a href="${resource.filePath}" target="_blank">
                        ${resource.title}
                    </a>
                </div>
            `;

        });

    })
    .catch(error => {

        console.error("Resource error:", error);

        materialSection.querySelector(".material-list").innerHTML =
            "<p>Unable to load resources.</p>";
    });
}
// ===============================
// SEMESTER TITLE
// ===============================

const semesterTitle = document.getElementById("semester-title");

if (semester) {
    semesterTitle.textContent = "Semester " + semester;
}

// ===============================
// GET SUBJECTS FROM BACKEND
// ===============================

const subjectsContainer =
    document.getElementById("subjects-container");

if (semester) {

    fetch("http://127.0.0.1:8080/api/subjects?semester=" + semester)

        .then(response => response.json())

        .then(subjects => {

            subjectsContainer.innerHTML = "";

            subjects.forEach(function(subject) {

    subjectsContainer.innerHTML += `
        <div class="subject-card"
            onclick="showMaterials('${subject.name}', ${subject.id})"

            <h2>${subject.name}</h2>

            <p>Notes, PYQs and study material.</p>

            <span>Explore →</span>

        </div>
    `;

});

        })

        .catch(error => {

            console.error("Backend error:", error);

            subjectsContainer.innerHTML =
                "<p>Unable to load subjects.</p>";
        });
}