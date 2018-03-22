window.onload = () => {

    // Login Event Listener
    document.getElementsById("login").addEventListener("click", () => {
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        // AJAX Logic
        let xhr = new XmlHttpRequest();

        xhr.onreadystatechange = () => {
            // If the request id DONE (4), and everything is OK
            if (xhr.readyState === 4 && xhr.status === 200) {
                // Getting JSON from response body
                let data = JSON.parse(xhr.responseText);
                console.log(data);

                // Call login response processing
            }
        };

        // Doing an HTTP to a specigic endpoint
        xhr.open("POST", `login.do?username=${username}&password=${password}`);

        // Sending our request
        xhr.send();
    });
}