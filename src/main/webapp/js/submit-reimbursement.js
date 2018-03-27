"use strict";

window.onload = () => {
    document.getElementById("usernameTitle").innerHTML = sessionStorage.getItem("employeeUsername");
    document.getElementById("submit").addEventListener("click", submitReimbursement);
    getReimbursementTypes();
};

let getReimbursementTypes = () => {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            let types = document.getElementById("type");
            for (let i = 0; i < data.length; i++) {
                let option = document.createElement("option");
                option.setAttribute("value", data[i].id);
                option.appendChild(document.createTextNode(data[i].type));
                types.appendChild(option);
            }
        }
    }
    xhr.open("GET", "reimbursement-types.do");
    xhr.send();
};

function submitReimbursement() {
    let amount = document.getElementById("amount").value;
    let description = document.getElementById("description").value;
    let typeSelect = document.getElementById("type");
    let type = typeSelect.options[typeSelect.selectedIndex].value;
    console.log(type);

    // AJAX Logic
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        // If the request id DONE (4), and everything is OK
        if (xhr.readyState === 4 && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            // Call login response processing
            if (data.success) {
                document.getElementById("message").innerHTML = `<span class="label label-success label-center">${data.message}</span>`;
            } else {
                document.getElementById("message").innerHTML = `<span class="label label-danger label-center">${data.message}</span>`;
            }
        }
    };
    xhr.open("POST", `submit-reimbursement.do?amount=${amount}&description=${description}&type=${type}`);
    xhr.send();
};