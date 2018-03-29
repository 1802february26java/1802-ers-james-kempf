"use strict";

window.onload = () => {
    document.getElementById("usernameTitle").innerHTML = sessionStorage.getItem("employeeUsername");
    document.getElementById("listSelector").addEventListener("change", getReimbursements);
    document.getElementById("employeeSelectorButton").addEventListener("click", getReimbursements);
    getInformation();
    getReimbursements();
    getEmployees();
}

function getInformation() {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            let informationTable = document.getElementById("informationTable");
            let rowData = [
                data.id,
                data.firstName + " " + data.lastName,
                data.username,
                data.email,
                data.employeeRole.type
            ];
            addRowToTable(rowData, informationTable)
        }
    };
    xhr.open("GET", "select-employee.do");
    xhr.send();
}

function getEmployees() {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            let informationTable = document.getElementById("employeeTable");
            console.log(data);
            data.forEach((employee) => {
                let rowData = [
                    employee.id,
                    employee.firstName + " " + employee.lastName,
                    employee.username,
                    employee.email,
                    employee.employeeRole.type
                ];
                addRowToTable(rowData, informationTable)
            });
        }
    };
    xhr.open("GET", "list-employees.do");
    xhr.send();
}

function getReimbursements() {
    document.getElementById("reimbursementTable").innerHTML = "";
    let listSelector = document.getElementById("listSelector");
    let list = listSelector.options[listSelector.selectedIndex].value;
    if (list === "userPending" || list === "allPending") {
        document.getElementById("approve-button-header").style.display = "table-cell";
        document.getElementById("approver-header").style.display = "none";
        document.getElementById("resolved-header").style.display = "none";
    } else {
        document.getElementById("approve-button-header").style.display = "none";
        document.getElementById("approver-header").style.display = "table-cell";
        document.getElementById("resolved-header").style.display = "table-cell";
    }
    let addButton = false;
    if (list === "userPending" || list === "userFinalized") {
        document.getElementById("employeeSelector").style.display = "block";
        let id = document.getElementById("employeeId").value;
        if (!isNaN(id) && id != "") {
            list += `&id=${id}`;
            makeCall();
        }
    } else {
        document.getElementById("employeeSelector").style.display = "none";
        makeCall();
    }
    function makeCall() {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4 && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                let reimbursementTable = document.getElementById("reimbursementTable");
                data.forEach((reimbursement) => {
                    if (list.startsWith("userPending") || list === "allPending") {
                        let rowData = [
                            reimbursement.id,
                            reimbursement.requester.firstName + " " + reimbursement.requester.lastName,
                            "$" + reimbursement.amount,
                            reimbursement.description,
                            reimbursement.status.status,
                            reimbursement.requested.monthValue + "-" + reimbursement.requested.dayOfMonth + "-" + reimbursement.requested.year
                        ];
                        addRowToTable(rowData, reimbursementTable, true);
                    } else {
                        let rowData = [
                            reimbursement.id,
                            reimbursement.requester.firstName + " " + reimbursement.requester.lastName,
                            "$" + reimbursement.amount,
                            reimbursement.description,
                            reimbursement.status.status,
                            (reimbursement.approver ? reimbursement.approver.firstName + " " + reimbursement.approver.lastName : ""),
                            reimbursement.requested.monthValue + "-" + reimbursement.requested.dayOfMonth + "-" + reimbursement.requested.year,
                            (reimbursement.resolved ? reimbursement.resolved.monthValue + "-" + reimbursement.resolved.dayOfMonth + "-" + reimbursement.resolved.year : ""),
                        ];
                        addRowToTable(rowData, reimbursementTable);
                    }
                });
            }
        };
        console.log(`select-multiple-reimbursements.do?list=${list}`);
        xhr.open("POST", `select-multiple-reimbursements.do?list=${list}`);
        xhr.send();
    }
};

function addRowToTable(rowData, table, addButton) {
    let tableRow = document.createElement("tr");
    for (let i = 0; i < rowData.length; i++) {
        let tableData = document.createElement("td");
        tableData.appendChild(document.createTextNode(rowData[i]));
        tableRow.appendChild(tableData);
    }
    if (addButton) {
        let tableData = document.createElement("td");
        let approveButton = document.createElement("input");
        approveButton.type = "button";
        approveButton.className = "btn btn-success approve-button";
        approveButton.value = "Y";
        approveButton.addEventListener("click", () => {
            finalize(3, rowData[0], tableRow);
        });
        tableData.appendChild(approveButton);
        let denyButton = document.createElement("input");
        denyButton.type = "button";
        denyButton.className = "btn btn-danger approve-button";
        denyButton.value = "N";
        denyButton.addEventListener("click", () => {
            finalize(2, rowData[0], tableRow);
        });
        tableData.appendChild(denyButton);
        tableRow.appendChild(tableData);
    }
    table.appendChild(tableRow);
};

function finalize(status, id, tableRow) {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            if (data.success) {
                tableRow.style.display = "none";
            }
        }
    };
    xhr.open("POST", `finalize.do?id=${id}&status=${status}`)
    xhr.send();
}