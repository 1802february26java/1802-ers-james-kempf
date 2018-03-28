"use-strict";

window.onload = () => {
    document.getElementById("usernameTitle").innerHTML = sessionStorage.getItem("employeeUsername");

    document.getElementById("submit").addEventListener("click", () => {
        let firstName = document.getElementById("firstName").value;
        let lastName = document.getElementById("lastName").value;
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;
        let repeatPassword = document.getElementById("repeatPassword").value;
        let email = document.getElementById("email").value;

        if (password !== repeatPassword) {
            document.getElementById("updateMessage").innerHTML = '<span class="label label-danger label-center">Password Mismatch</span>';
        } else {
             // AJAX Logic
            let xhr = new XMLHttpRequest();

            xhr.onreadystatechange = () => {
                // If the request id DONE (4), and everything is OK
                if (xhr.readyState === 4 && xhr.status === 200) {
                    let data = JSON.parse(xhr.responseText);
                    // Call login response processing
                   if (data.message === "Update successful") {
                        document.getElementById("message").innerHTML = `<span class="label label-success label-center">${data.message}</span>`;
                    } else {
                        document.getElementById("message").innerHTML = `<span class="label label-danger label-center">${data.message}</span>`;
                    }
                }
            };

            // Doing an HTTP to a specigic endpoint
            xhr.open("POST", `update-employee.do?firstName=${firstName}&lastName=${lastName}&username=${username}&password=${password}&email=${email}`);

            // Sending our request
            xhr.send();
        }
    });
}