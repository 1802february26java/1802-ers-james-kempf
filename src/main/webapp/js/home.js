"use strict";

window.onload = () => {
    document.getElementById("usernameTitle").innerHTML = sessionStorage.getItem("employeeUsername");
    getReimbursements();
}

function getReimbursements() {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            populateList(data);
        }
    };
    xhr.open("POST", "select-multiple-reimbursements.do?list=current");
    xhr.send();
};

function populateList(data) {
    let reimbursementTable = document.getElementById("reimbursementTable");
    data.forEach((reimbursement) => {
        let rowData = [
            reimbursement.requester.firstName + " " + reimbursement.requester.lastName,
            "$" + reimbursement.amount,
            reimbursement.description,
            reimbursement.status.status,
            (reimbursement.approver ? reimbursement.approver.firstName + " " + reimbursement.approver.lastName : ""),
            reimbursement.requested.monthValue + "-" + reimbursement.requested.dayOfMonth + "-" + reimbursement.requested.year,
            (reimbursement.resolved ? reimbursement.resolved.monthValue + "-" + reimbursement.resolved.dayOfMonth + "-" + reimbursement.resolved.year : ""),
        ];
        let tableRow = document.createElement("tr");
        for (let i = 0; i < rowData.length; i ++) {
            let tableData = document.createElement("td");
            tableData.appendChild(document.createTextNode(rowData[i]));
            tableRow.appendChild(tableData);
        }
        reimbursementTable.appendChild(tableRow);
    });
};