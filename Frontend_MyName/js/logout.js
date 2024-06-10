function logout(){
    storage.removeItem("ID");
    window.location.replace("http://127.0.0.1:5500/templates/login.html#");
}