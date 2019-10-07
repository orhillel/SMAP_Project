

function addUser(){    
    var email = document.getElementById('exampleInputEmail1').value;
    if(email == ""){
        alert('Email field is empty!')
        return false;
    }
    var password = document.getElementById("exampleInputPassword1").value
    if(password == ""){
        alert('Password field is empty!')
        return false;
    }
    if(password.length < 6 ){
        alert('You have to enter at least 6 digit Password!')
        return false;
    }
    var userToAdd = {
        'email':email,
        'password':password
    }
    if(!checkIfUserExsits(userToAdd)){
        addUserToDB(userToAdd);
        return false;
    }
    return false;
}

function addUserToDB(user){
    var url = "https://smapapp.azurewebsites.net/api/addUser?code=wckEGxTgDQiyWeEIZ1ab1Jam5I4ow0Tial80QfP58JYSo/QNy9m0OA==";
    httpGetAsyncAddUser(url,function trueAllways(params) {
        return false;
    },user);
    return false;
}


function checkIfUserExsits(user){
    var url = "https://smapapp.azurewebsites.net/api/getUser?code=kUTf0LKank7Sm04Ey04rNYE0tqt4J7cdLTvxPl9b4oYWPLzDf/32uA==";
    return httpGetSyncCheckUser(url,addUserToDB,user);
}

function httpGetAsyncAddUser(theUrl, callback,user)
{
    var xhr = new XMLHttpRequest();
    xhr.open("POST", theUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            alert('User added');
            callback(user);
        }
    };
    var data = JSON.stringify(user);
    xhr.send(data);
}

function httpGetSyncCheckUser(theUrl, callback,user)
{
    var xhr = new XMLHttpRequest();
    xhr.open("POST", theUrl, false);
    xhr.setRequestHeader("Content-Type", "application/json");
    var data = JSON.stringify(user);
    xhr.send(data);
    if (xhr.status === 200) {
        var json = JSON.parse(xhr.responseText);
        if(json.m_StringValue != ""){
            alert('User Already Exsits');
            return true;
        }else{
        return false;
        }
    }
}