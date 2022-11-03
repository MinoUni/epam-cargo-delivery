const form = document.querySelector("form"),
    passwordField = form.querySelector(".password-field"),
    passwordInput = passwordField.querySelector(".password"),
    confirmPasswordField = form.querySelector(".confirm-password-field"),
    confirmPasswordInput = confirmPasswordField.querySelector(".confirm-password");

form.addEventListener("submit", (e) => {
    e.preventDefault();
    if (passwordInput.value !== confirmPasswordInput.value) {
        return alert("Passwords didn't match!");
    }
    form.submit();
});

