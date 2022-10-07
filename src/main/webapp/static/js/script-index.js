const body = document.querySelector("body"),
    header = document.querySelector("nav"),
    modeToggle = document.querySelector(".dark-light"),
    sidebarClose = document.querySelector(".sidebar-close"),
    sidebarOpen = document.querySelector(".sidebar-open");

let getTheme = localStorage.getItem("theme");

if (getTheme && getTheme === "dark") {
    body.classList.add("dark");
}

// change website themes {dark, light}
modeToggle.addEventListener("click", () => {
    modeToggle.classList.toggle("active");
    body.classList.toggle("dark");

    // keep selected theme even if user refresh page
    if (body.classList.contains("dark")) {
        localStorage.setItem("theme", "dark");
    } else {
        localStorage.setItem("theme", "light");
    }
});

// toggle sidebar
sidebarOpen.addEventListener("click", () => {
    header.classList.add("active");
});

body.addEventListener("click", (e) => {
    let clickedElm = e.target;

    if (
        !clickedElm.classList.contains("sidebar-open") &&
        !clickedElm.classList.contains("menu")
    ) {
        header.classList.remove("active");
    }
});

