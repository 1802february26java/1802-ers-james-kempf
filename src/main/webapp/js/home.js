window.onload = () => {
    // AJAX Logic
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        // If the request id DONE (4), and everything is OK
        if (xhr.readyState === 4 && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            populateList(data);
        }
    };

    // Doing an HTTP to a specific endpoint
    xhr.open("POST", "select-multiple-reimbursements.do?list=current");

    // Sending our request
    xhr.send();
}

{/* <th>Requester</th>
<th>Status</th>
<th>Amount</th>
<th>Description</th>
<th>Approver</th>
<th>Date</th> */}

function populateList(data) {
    let reimbursementTable = document.getElementById("reimbursementTable");
    data.forEach((reimbursement) => {
        rowData = [
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