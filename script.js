// ===============================
// API CONFIGURATION
// ===============================
const API_BASE_URL = "https://campusconnect-8rzv.onrender.com";

// ===============================
// UPDATE NAVBAR BASED ON LOGIN
// ===============================

const authToken = localStorage.getItem("authToken");
const userName = localStorage.getItem("userName");

const navLinks = document.querySelector(".nav-links");

if (navLinks && authToken) {

    const signInButton =
        navLinks.querySelector(".signin-btn");

    const joinButton =
        navLinks.querySelector(".join-btn");

    if (signInButton) {
        signInButton.remove();
    }

    if (joinButton) {
        joinButton.remove();
    }

    // ME BUTTON
    const meButton =
        document.createElement("a");

    meButton.href = "#";

    meButton.textContent =
        "👤 " + (userName || "Me");

    meButton.className = "me-btn";

    meButton.onclick = function(event) {

        event.preventDefault();

        showProfile();
    };

    navLinks.appendChild(meButton);


    // LOGOUT BUTTON
    const logoutButton =
        document.createElement("a");

    logoutButton.href = "#";

    logoutButton.textContent =
        "Logout";

    logoutButton.className =
        "logout-btn";

    logoutButton.onclick =
        function(event) {

            event.preventDefault();

            localStorage.removeItem(
                "authToken"
            );

            localStorage.removeItem(
                "userName"
            );

            localStorage.removeItem(
                "userEmail"
            );

            localStorage.removeItem(
                "userRole"
            );

            window.location.href =
                "index.html";
        };

    navLinks.appendChild(logoutButton);
}


// ===============================
// SHOW USER PROFILE
// ===============================

function showProfile() {
    window.location.href = "profile.html";

}


// ===============================
// WELCOME USER
// ===============================

const welcomeUser =
    document.getElementById(
        "welcome-user"
    );

if (welcomeUser) {

    const currentUserName =
        localStorage.getItem(
            "userName"
        );

    if (currentUserName) {

        welcomeUser.textContent =
            "Welcome back, " +
            currentUserName +
            " 👋";
    }
}


// ===============================
// GET SEMESTER FROM URL
// ===============================

const urlParams =
    new URLSearchParams(
        window.location.search
    );

const semester =
    urlParams.get("semester");


// ===============================
// SHOW STUDY MATERIAL
// ===============================

function showMaterials(
    subject,
    subjectId
) {

    const materialSection =
        document.getElementById(
            "material-section"
        );

    if (!materialSection) {
        return;
    }

    materialSection.style.display =
        "block";

    materialSection.innerHTML = `

        <h2>
            📚 ${subject} Study Material
        </h2>

        <div class="material-list">

            <p>
                Loading resources...
            </p>

        </div>
    `;


    fetch(
        "https://campusconnect-8rzv.onrender.com/api/resources?subjectId="
        + subjectId
    )

    .then(response => {

        if (!response.ok) {

            throw new Error(
                "Resources loading failed"
            );
        }

        return response.json();
    })

    .then(resources => {

        const materialList =
            materialSection.querySelector(
                ".material-list"
            );

        materialList.innerHTML = "";


        if (resources.length === 0) {

            materialList.innerHTML =
                "<p>No resources available for this subject yet.</p>";

            return;
        }


       resources.forEach(
    function(resource) {

        materialList.innerHTML += `

            <div class="material-item">

                <div>

                    📄

                    <a
                        href="${resource.filePath}"
                        target="_blank"
                        onclick="trackRecentlyViewed(${resource.id})"
                    >
                        ${resource.title}
                    </a>

                </div>


                <button
                    class="save-resource-btn"
                    onclick="saveResource(${resource.id})"
                >
                    ⭐ Save
                </button>

            </div>

        `;
    }
);
    })

    .catch(error => {

        console.error(
            "Resource error:",
            error
        );

        materialSection.querySelector(
            ".material-list"
        ).innerHTML =

            "<p>Unable to load resources.</p>";
    });
}


// ===============================
// SEMESTER TITLE
// ===============================

const semesterTitle =
    document.getElementById(
        "semester-title"
    );

if (
    semester &&
    semesterTitle
) {

    semesterTitle.textContent =
        "Semester " + semester;
}


// ===============================
// GET SUBJECTS FROM BACKEND
// ===============================

const subjectsContainer =
    document.getElementById(
        "subjects-container"
    );

if (
    semester &&
    subjectsContainer
) {

    fetch(
        "https://campusconnect-8rzv.onrender.com/api/subjects?semester="
        + semester
    )

    .then(response => {

        if (!response.ok) {

            throw new Error(
                "Subjects loading failed"
            );
        }

        return response.json();
    })

    .then(subjects => {

        subjectsContainer.innerHTML =
            "";


        if (subjects.length === 0) {

            subjectsContainer.innerHTML =
                "<p>No subjects available.</p>";

            return;
        }


        subjects.forEach(
            function(subject) {

                subjectsContainer.innerHTML += `

                    <div
                        class="subject-card"
                        onclick="showMaterials('${subject.name}', ${subject.id})"
                    >

                        <h2>
                            ${subject.name}
                        </h2>

                        <p>
                            Notes, PYQs and study material.
                        </p>

                        <span>
                            Explore →
                        </span>

                    </div>

                `;
            }
        );
    })

    .catch(error => {

        console.error(
            "Backend error:",
            error
        );

        subjectsContainer.innerHTML =
            "<p>Unable to load subjects.</p>";
    });
}

// ===============================
// SAVE RESOURCE
// ===============================

function saveResource(resourceId) {

    fetch(
        "https://campusconnect-8rzv.onrender.com/api/saved-resources/"
        + resourceId,
        {
            method: "POST",

            headers: {
                "Authorization":
                    "Bearer " + authToken
            }
        }
    )

    .then(async response => {

        const data =
            await response.text();

        if (!response.ok) {
            throw new Error(data);
        }

        return data;
    })

    .then(() => {

        alert(
            "⭐ Resource saved successfully!"
        );

    })

    .catch(error => {

        console.error(
            "Save resource error:",
            error
        );

        if (
            error.message ===
            "Resource already saved"
        ) {

            alert(
                "⭐ This resource is already saved."
            );

        } else {

            alert(
                "Unable to save resource."
            );

        }

    });
}
// ===============================
// CAMPUSAI CHATBOT
// ===============================

function toggleCampusAI() {

    const chat =
        document.getElementById("campus-ai-chat");

    if (!chat) {
        return;
    }

    if (chat.style.display === "flex") {
        chat.style.display = "none";
    } else {
        chat.style.display = "flex";
    }
}
function sendCampusAIMessage() {

    const input = document.getElementById("campus-ai-input");
    const messages = document.getElementById("campus-ai-messages");

    const message = input.value.trim();

    if (!message) return;

    // Create history if it doesn't exist
    if (!window.campusAIHistory) {
        window.campusAIHistory = [];
    }

    // Add user message to history
    window.campusAIHistory.push({
        role: "user",
        text: message
    });

    // Show user message
    messages.innerHTML += `
        <div class="user-message">
            ${message}
        </div>
    `;

    input.value = "";

    // Show thinking message
    const loadingMessage = document.createElement("div");

    loadingMessage.className = "ai-message";

    loadingMessage.innerHTML =
        `<span class="ai-typing">
            🤖 CampusAI is thinking
            <span class="typing-dots">...</span>
        </span>`;

    messages.appendChild(loadingMessage);

    messages.scrollTop = messages.scrollHeight;

    // Send complete conversation history
    fetch("https://campusconnect-8rzv.onrender.com/api/ai/chat", {

        method: "POST",

       headers: {
    "Content-Type": "application/json",
    "Authorization": "Bearer " + localStorage.getItem("token")
},

        body: JSON.stringify({
            history: window.campusAIHistory
        })

    })
    .then(async response => {

        const data = await response.json();

        if (!response.ok) {
            throw new Error(
                data.answer || "AI request failed"
            );
        }

        return data;
    })
    .then(data => {

        // Add AI response to conversation history
        window.campusAIHistory.push({
            role: "model",
            text: data.answer
        });

        loadingMessage.innerHTML = `
            <div class="ai-answer-content">
                🤖 ${formatAIResponse(data.answer)}
            </div>

            <button
                class="copy-ai-btn"
                onclick="copyAIAnswer(this)">
                📋 Copy
            </button>
        `;

        loadingMessage.dataset.answer =
            data.answer;

        messages.scrollTop =
            messages.scrollHeight;
    })
    .catch(error => {

        console.error(
            "CampusAI error:",
            error
        );

        // Remove failed user message from history
        window.campusAIHistory.pop();

        loadingMessage.textContent =
            "❌ CampusAI error: " +
            error.message;
    });
}
// ===================
// FORMAT AI RESPONSE
// ===============================
function formatAIResponse(text) {
    // Escape HTML first
    text = text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");

    // Code blocks
    text = text.replace(
        /```(\w+)?\s*([\s\S]*?)```/g,
        function(match, language, code) {

            const lang = language || "code";

            return `
                <div class="code-block">
                    <div class="code-header">
                        <span>${lang}</span>
                        <button onclick="copyCode(this)">
                            📋 Copy
                        </button>
                    </div>
                    <pre><code>${code.trim()}</code></pre>
                </div>
            `;
        }
    );

    // Headings
    text = text.replace(/^### (.*)$/gm, "<h4>$1</h4>");
    text = text.replace(/^## (.*)$/gm, "<h3>$1</h3>");
    text = text.replace(/^# (.*)$/gm, "<h2>$1</h2>");

    // Bold / italic
    text = text.replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>");
    text = text.replace(/\*(.*?)\*/g, "<em>$1</em>");

    // Lists
    text = text.replace(/^\* (.*)$/gm, "<li>$1</li>");
    text = text.replace(/^\d+\.\s+(.*)$/gm, "<li>$1</li>");

    // New lines
    text = text.replace(/\n/g, "<br>");

    return text;
}
function maximizeCampusAI() {

    const chat =
        document.getElementById("campus-ai-chat");

    if (!chat) {
        return;
    }

    chat.classList.toggle("ai-maximized");
}
function newCampusAIChat() {

    const messages =
        document.getElementById("campus-ai-messages");

    if (!messages) return;

    // Clear conversation history
    window.campusAIHistory = [];

    messages.innerHTML = `
        <div class="ai-message">
            👋 Hi! I'm CampusAI.<br>
            Ask me anything about your studies.
        </div>
    `;

    const input =
        document.getElementById("campus-ai-input");

    if (input) {
        input.value = "";
        input.focus();
    }
}
function copyAIAnswer(button) {

    const message =
        button.parentElement.dataset.answer;

    navigator.clipboard.writeText(message);

    button.textContent = "✅ Copied";

    setTimeout(function() {
        button.textContent = "📋 Copy";
    }, 1500);
}
function copyCode(button) {
    const code = button
        .closest(".code-block")
        .querySelector("code")
        .textContent;

    navigator.clipboard.writeText(code);

    button.textContent = "✅ Copied";

    setTimeout(function() {
        button.textContent = "📋 Copy";
    }, 1500);
}