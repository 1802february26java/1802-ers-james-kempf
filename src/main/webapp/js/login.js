window.onload = () => {

    // Login Event Listener
    document.getElementById("login").addEventListener("click", () => {
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        // AJAX Logic
        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = () => {
            // If the request id DONE (4), and everything is OK
            if (xhr.readyState === 4 && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                // Call login response processing
                login(data);
            }
        };

        // Doing an HTTP to a specigic endpoint
        xhr.open("POST", `login.do?username=${username}&password=${password}`);

        // Sending our request
        xhr.send();
    });
}

function login(data) {
    // If message is a member of the JSON, it was AUTHENTICATION FAILED
    if(data.message) {
        document.getElementById("loginMessage").innerHTML = '<span class="label label-danger label-center">Wrong credentials.</span>';
    }
    else {
        // Using sessionStorage of JavaScript
        sessionStorage.setItem("employeeId", data.id);
        sessionStorage.setItem("employeeUsername", data.username);

        // Redirect to Home page
        window.location.replace("home.do");
    }
}