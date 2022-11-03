const body = document.querySelector("body"),
    theme = document.querySelector(".theme"),
    getTheme = sessionStorage.getItem("theme");

if (getTheme && getTheme === "dark") {
    body.classList.add("dark");
}

theme.addEventListener("click", () => {
    theme.classList.toggle("active");
    body.classList.toggle("dark");

    if (body.classList.contains("dark")) {
        sessionStorage.setItem("theme", "dark");
    } else {
        sessionStorage.setItem("theme", "light");
    }
});