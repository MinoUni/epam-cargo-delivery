const form = document.querySelector("form"),
    loginField = form.querySelector(".login-field"),
    loginInput = loginField.querySelector(".login"),
    passwordField = form.querySelector(".password-field"),
    passwordInput = passwordField.querySelector(".password"),
    passwordShowAndHide = document.querySelectorAll(".show-pass");

passwordShowAndHide.forEach((showPass) => {
    showPass.addEventListener("click", () => {
        let passFields =
            showPass.parentElement.parentElement.querySelectorAll(".password");

        passFields.forEach((password) => {
            if (password.type === "password") {
                password.type = "text";
                showPass.classList.replace("bx-hide", "bx-show");
                return;
            }
            password.type = "password";
            showPass.classList.replace("bx-show", "bx-hide");
        });
    });
});

function validateLogin() {
    if (loginInput.value == null || loginInput.value === "") {
        return loginField.classList.add("invalid");
    }
    loginField.classList.remove("invalid");
}

function validatePassword() {
    if (passwordInput.value == null || passwordInput.value === "") {
        return passwordField.classList.add("invalid");
    }
    passwordField.classList.remove("invalid");
}

form.addEventListener("submit", (e) => {
    e.preventDefault();
    validateLogin();
    validatePassword();

    if (loginField.classList.contains("invalid") || passwordField.classList.contains("invalid")) {
        form.reset();
    } else {
        form.submit();
    }
});