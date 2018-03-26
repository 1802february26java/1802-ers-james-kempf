window.onload = () => {
    document.getElementById("usernameTitle").innerHTML = sessionStorage.getItem("employeeUsername");
    document.getElementById("name").innerHTML = sessionStorage.getItem("employeeFirstName") + " " + sessionStorage.getItem("employeeLastName");
    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeUsername");
    document.getElementById("email").innerHTML = sessionStorage.getItem("employeeEmail");
}