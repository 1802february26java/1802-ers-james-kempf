"use-strict";

window.onload = () => {
    document.getElementById("usernameTitle").innerHTML = sessionStorage.getItem("employeeUsername");
    document.getElementById("register").addEventListener("click", registerEmployee);
}
function registerEmployee() {
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let repeatPassword = document.getElementById("repeatPassword").value;
    let email = document.getElementById("email").value;
    let roleSelect = document.getElementById("roleSelect");
    let role = roleSelect.options[roleSelect.selectedIndex].value;

    if (password !== repeatPassword) {
        document.getElementById("message").innerHTML = '<span class="label label-danger label-center">Password Mismatch</span>';
    } else {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4 && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                if (data.success) {
                    document.getElementById("message").innerHTML = `<span class="label label-success label-center">${data.message}</span>`;
                } else {
                    document.getElementById("message").innerHTML = `<span class="label label-danger label-center">${data.message}</span>`;
                }
            }
        };
        xhr.open("POST", `register.do?firstName=${firstName}&lastName=${lastName}&username=${username}&password=${password}&email=${email}&role=${role}`);
        xhr.send();
    }
};