const form = document.querySelector("form"),
    loginField = form.querySelector(".login-field"),
    loginInput = loginField.querySelector(".login"),
    nameField = form.querySelector(".name-field"),
    nameInput = nameField.querySelector(".name"),
    surnameField = form.querySelector(".surname-field"),
    surnameInput = surnameField.querySelector(".surname"),
    emailField = form.querySelector(".email-field"),
    emailInput = emailField.querySelector(".email"),
    passwordField = form.querySelector(".password-field"),
    passwordInput = passwordField.querySelector(".password"),
    confirmPasswordField = form.querySelector(".confirm-password-field"),
    confirmPasswordInput = confirmPasswordField.querySelector(".confirm-password"),
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

function validateName() {
    if (nameInput.value == null || nameInput.value === "") {
        return nameField.classList.add("invalid");
    }
    nameField.classList.remove("invalid");
}

function validateSurname() {
    if (surnameInput.value == null || surnameInput.value === "") {
        return surnameField.classList.add("invalid");
    }
    surnameField.classList.remove("invalid");
}

function validateEmail() {
    const pattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
    if (!emailInput.value.match(pattern)) {
        return emailField.classList.add("invalid");
    }
    emailField.classList.remove("invalid");
}

function validatePassword() {
    if (passwordInput.value == null || passwordInput.value === "") {
        return passwordField.classList.add("invalid");
    }
    passwordField.classList.remove("invalid");
}

function comparePasswords() {
    if (passwordInput.value !== confirmPasswordInput.value || confirmPasswordInput.value === "" ||
        confirmPasswordInput.value == null) {
        return confirmPasswordField.classList.add("invalid");
    }
    confirmPasswordField.classList.remove("invalid");
}

form.addEventListener("submit", (e) => {
    e.preventDefault();
    validateLogin();
    validateName();
    validateSurname();
    validateEmail();
    validatePassword();
    comparePasswords();

    if (loginField.classList.contains("invalid") ||
        nameField.classList.contains("invalid") ||
        surnameField.classList.contains("invalid") ||
        emailField.classList.contains("invalid") ||
        passwordField.classList.contains("invalid") ||
        confirmPasswordField.classList.contains("invalid")
    ) {
        form.reset();
    } else {
        form.submit();
    }
});

