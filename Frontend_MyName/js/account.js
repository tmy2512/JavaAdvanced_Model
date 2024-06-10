// param để search và filter cho account
var getApiAccountParam = {
    pageNumber: 0,
    search: "",
    role: "",
    departmentName: "",
    getParamString() {
        return "?pageNumber=" + this.pageNumber +
            "&search=" + this.search +
            "&role=" + this.role +
            "&departmentName=" + this.departmentName;
    }
};

// f5 lại bảng account
function refreshAccountTable(){
    initAccountTable("");
}

// ẩn modal tạo mới account
function hideAddNewAccountModal(){
    $("#add-new-account-modal").modal("hide");
}
// khởi tạo danh sách account có filter
function initAccountTableWithFilter() {
    let role = document.getElementById("account-role-filter").value;
    let departmentName = document.getElementById("account-department-filter").value;
    console.log(role);
    console.log(departmentName);
    getApiAccountParam.role = role;
    getApiAccountParam.departmentName = departmentName;
    initAccountTable(getApiAccountParam.getParamString());
}

// tìm account theo phím enter
function searchAccountByEnterKey() {
    $("#search-account-by-name").keydown((event) => {
        let keyCode = event.which
        if (keyCode == 13) {
            searchAccountByName();
        }
    })

}

// tìm account theo name
function searchAccountByName() {
    var name = document.getElementById("search-account-by-name").value;
    getApiAccountParam.search = name;
    // lấy param rồi khởi tạo bảng
    initAccountTable(getApiAccountParam.getParamString());
}

// phân trang cho account
function paginationAccount(result) {
    const activeNumber = result.number + 1;
    $('#pagination-account').html(`<a title="Previous" href = "javascript:void(0)" onclick="toAccountPage(${activeNumber > 1 ? activeNumber - 1 : activeNumber})">Previous</a>`);
    for (let i = 0; i < result.totalPages; i++) {
        const curNumber = i + 1;
        if (curNumber == activeNumber) {
            $('#pagination-account').append(`<a href = "javascript:void(0)" class="active">${curNumber}</a>`);
        } else {
            $('#pagination-account').append(`<a href = "javascript:void(0)" onclick="toAccountPage(${curNumber})">${curNumber}</a>`);
        }
    }
    $('#pagination-account').append(`<a href = "javascript:void(0)" title="Next" onclick="toAccountPage(${activeNumber < result.totalPages ? activeNumber + 1 : activeNumber})">Next</a>`);

}


// hiện modal xóa account
function openDeleteAccountModal(accountID) {
    $("#delete-account-modal").modal("show");
    document.getElementById("delete-account-id").value = accountID;
    console.log(document.getElementById("delete-account-id").value);
}

// ẩn modal xóa account
function hideDeleteAccountModal() {
    $("#delete-account-modal").modal("hide");
}

// lấy mảng id của những account sẽ xóa
function getArrayIdAccountsToDelete() {
    var listIDs = new Array();
    var checkboxes = document.getElementsByName('check-input');
    for (var checkbox of checkboxes) {
        if (checkbox.checked) {
            listIDs.push(checkbox.value);
        }
    }
    return listIDs;
}

// xóa từng account
function deleteAccount() {
    let id = document.getElementById("delete-account-id").value;
    $.ajax({
        url: apiAccountURL + "/" + id,
        type: 'DELETE',
        async: false,
        success: function (result) {
        }
    });
    hideDeleteAccountModal();
    initAccountTable("");
    showAlertSuccess();
}

// xóa nhiều department
function deleteAccounts() {
    let ids = getArrayIdAccountsToDelete();
    // console.log(ids);
    let data = JSON.stringify({ids: ids});
    // console.log(data)

    let isOk = true;
    $.ajax({
        url: apiAccountURL,
        type: 'DELETE',
        data: data, // body
        contentType: "application/json",
        async: false,
        success: function (result) {
            isOk = true;
        },
        error: function(xhr){
            isOk = false;
            console.log(xhr.responseJSON.message);
            const errs = xhr.responseJSON.error;
            let errorList = "";
            for (const key in errs) {
                if (Object.hasOwnProperty.call(errs, key)) {
                    const element = errs[key];
                    errorList += element  + "\n";
                }
            }
             alert(errorList);
        }
    });
    if(isOk == true) {
        hideDeleteAccountsModal();
        showAlertSuccess();

    }
    initAccountTable("");
}

// ẩn modal xóa nhiều account
function hideDeleteAccountsModal() {
    $("#delete-accounts-modal").modal("hide");
}

// mở modal xóa nhiều account
function openDeleteAccountsModal(){
    $("#delete-accounts-modal").modal("show");
}

// chuyển đến trang số ***
function toAccountPage(number) {
    getApiAccountParam.pageNumber = number;
    initAccountTable(getApiAccountParam.getParamString());
}

// khởi tạo danh sách role cho bảng account
function initAccountRoleList() {
    $('.form-role-select').empty();
    $('.form-role-select').html(`<option value="">--Choose a type--</option>`);
    $.ajax({
        url: apiAccountURL + "/roles",
        type: 'GET',
        success: function (result) {
            console.log(result);
            result.forEach(function (item) {
                $('.form-role-select').append(
                    '<option value="' + item.roleValue + '">' + item.roleName+ '</option>'
                )
            });
        }
    });

}

// khởi tạo danh sách department cho bảng account
function initAccountDepartmentList() {
    $('.form-department-select').empty();
    $('.form-department-select').html(`<option value="">--Choose a type--</option>`);
    $.ajax({
        url: apiDepartmentURL + "/lists",
        type: 'GET',
        async: false,
        success: function (result) {
            result.forEach(function (item) {
                $('#account-department-filter').append(
                    '<option value="' + item.name + '">' + item.name + '</option>'
                )
                $('#account-department-select').append(
                    '<option value="' + item.id + '">' + item.name + '</option>'
                )
            });
        }
    });

}


// khởi tạo danh sách role và danh sách department lúc tạo mới account
function openAddNewAccountModal() {
    $("#add-new-account-modal").modal("show");
    initAccountRoleList();
    initAccountDepartmentList();
    resetAccountModal();
}

// reset account modal
function resetAccountModal() {
    $(".modal-body > input").val("");
    $(".modal-body > select").val("");
}

// khởi tạo danh sách department cho drop box của account
function initDapartmentList() {
    $('#department-select').empty();
    $.ajax({
        url: apiDepartmentURL,
        type: 'GET',
        success: function (result) {
            var list = result.content;
            list.forEach(function (item) {
                $('#department-select').append(
                    '<option value="' + item.id + '">' + item.name + '</option>'
                )
            });
        }
    });

}

// lấy danh sách account
function initAccountTable(param) {
    $('tbody').empty();
    $.ajax({
        url: apiAccountURL + param,
        type: 'GET',
        success: function (result) {
            paginationAccount(result);
            var list = result.content;
            list.forEach(function (item) {
                $('tbody').append(
                    `<tr>
                    <td><input type='checkbox' name='check-input' value='${item.id}'></td>
                    <td>${item.username}</td>'
                    <td>${item.fullName}</td>
                    <td>${item.role}</td>
                    <td>${item.departmentName}</td>
                    <td>
                    <a class="edit" title="Edit" data-toggle="tooltip" onclick ="opendUpdateAccountModal(${item.id})"><i class="material-icons">&#xE254;</i></a>
                    <a class="delete" title="Delete" data-toggle="tooltip" onclick ="openDeleteAccountModal(${item.id})"><i class="material-icons">&#xE872;</i></a>
                    </td>
                    </tr>`
                )
            });
        }

        
    });
}

// tạo mới account
function createNewAccount() {
    let username = document.getElementById("account-username").value;
    let firstName = document.getElementById("account-first-name").value;
    let lastName = document.getElementById("account-last-name").value;
    let role = document.getElementById("account-role-select").value;
    let departmentId = document.getElementById("account-department-select").value;
    let email = username + "@gmail.com";
    let account = {
        username: username,
        firstName: firstName,
        lastName: lastName,
        // password mặc định để là 123456
        password: "123456",
        email: email,
        role: role,
        departmentId: departmentId
    }

    let isOk = true;

    let validMessage ="";
    if(username.length < 8 || username.length > 20){ 
        validMessage += "username's length is not valid\n";
    }
    if(firstName.length > 50 || firstName.length < 1){ 
        validMessage += "first name's length is not valid\n";
    }
    if(lastName.length > 50 || lastName.length < 1){ 
        validMessage += "last name's length is not valid\n";
    }

    if(role == undefined){
        validMessage += "role must not be null\n"
    }
    if(departmentId == undefined){
        validMessage += "department id must not be null"
    }

    if(validMessage.length != 0){
        alert(validMessage);
        // console.log(validMessage);
        return;
    }else {
        isOk = true;
    }
    let isCheck = true;
    if(isOk == true){
        $.ajax({
            url: apiAccountURL,
            type: 'POST',
            data: JSON.stringify(account), // body
            contentType: "application/json",
            async: false,
            success: function (result) {
                isCheck = true;
            },
            error: function(xhr){
                isCheck = false;
                console.log(xhr.responseJSON.message);
                const errs = xhr.responseJSON.error;
                let errorList = "";
                for (const key in errs) {
                    if (Object.hasOwnProperty.call(errs, key)) {
                        const element = errs[key];
                        errorList += element  + "\n";
                    }
                }
                 alert(errorList);
            }
        });

    }

    initAccountTable("");
    if(isCheck == true) {
        showAlertSuccess();
        hideAddNewAccountModal();
    }
}

// lưu account (update account or create new account), nếu có id thì update, nếu ko có thì tạo mới
function saveAccount() {
    let accountID = document.getElementById("account-id").value;
    // console.log(accountID);

    if (accountID == undefined || accountID == "") {
        createNewAccount();
    } else {
        updateAccount(accountID);
    }
    // showAlertSuccess();
}

var accountTemp;

// hiện modal update account
function opendUpdateAccountModal(accountID) {
    openAddNewAccountModal();
    $("#add-new-account-modal h5").html("Update Account")
    // gọi api đổ thông tin vào account
    $.ajax({
        url: apiAccountURL + "/" + accountID,
        type: 'GET',
        async: false,
        success: function (result) {
            accountTemp = result;
            console.log(accountTemp);
            //fill data
            document.getElementById("account-id").value = result.id;
            document.getElementById("account-username").value = result.username;
            document.getElementById("account-first-name").value = result.firstName;
            document.getElementById("account-last-name").value = result.lastName;
            document.getElementById("account-role-select").value = result.role;
            document.getElementById("account-department-select").value = result.departmentId;
        }
    });
}

// update account
function updateAccount(accountID) {
    console.log(accountTemp);

    var username = document.getElementById("account-username").value;
    var firstName = document.getElementById("account-first-name").value;
    var lastName = document.getElementById("account-last-name").value;
    var role = document.getElementById("account-role-select").value;
    var departmentId = document.getElementById("account-department-select").value;
    accountTemp.password = 
    accountTemp.username = username;
    accountTemp.firstName = firstName;
    accountTemp.lastName = lastName;
    accountTemp.role = role;
    accountTemp.departmentId = departmentId;

    console.log(accountTemp);

    if(accountTemp.username.length < 8 || accountTemp.username.length > 20){ 
        alert("username's length is not valid");
        return;
    }
    if(accountTemp.firstName.length > 50 || accountTemp.firstName.length < 1){ 
        alert("first name's length is not valid");
        return;
    }
    if(accountTemp.lastName.length > 50 || accountTemp.lastName.length < 1){ 
        alert("last name's length is not valid");
        return;
    }

    let isOk = true;
    // gọi api thêm mới account
    $.ajax({
        url: apiAccountURL,
        type: 'PUT',
        data: JSON.stringify(accountTemp), // body
        contentType: "application/json",
        async: false,   
        success: function (result) {
            isOk = true;

        },
        error: function(xhr){
            isOk = false;
            console.log(xhr.responseJSON.message);
            const errs = xhr.responseJSON.error;
            let errorList = "";
            for (const key in errs) {
                if (Object.hasOwnProperty.call(errs, key)) {
                    const element = errs[key];
                    errorList += element  + "\n";
                }
            }
             alert(errorList);
        }
    });
    if(isOk == true) {
        hideAddNewAccountModal();
        showAlertSuccess();
    }

    initAccountTable("");
}
