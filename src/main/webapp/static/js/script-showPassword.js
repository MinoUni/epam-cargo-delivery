const passwordShowAndHide = document.querySelectorAll(".show-pass");

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