import "./App.css";

function App() {


    const loginReqBody = {
        "username" : "admin",
        "password" : "admin",
    }

    fetch("http://localhost:8080/api/auth/login",{
        headers : {
            "Content-Type" : "application/json"
        },
        method : "post",
        body : JSON.stringify(loginReqBody)
    }).then((response) => Promise.all([response.text(),response.headers]))
        .then(([text,headers]) => {
            console.log(text);
            console.log(headers.get("Authorization"))
        });


    return (
        <div className="app">
        SIEMA
        </div>
    );
}

export default App;