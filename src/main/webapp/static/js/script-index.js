const calcForm = document.querySelector("#calcForm"),
	length = document.querySelector("#ilength"),
	width = document.querySelector("#width"),
	height = document.querySelector("#height"),
	weight = document.querySelector("#weight"),
	priceInput = document.querySelector("#pr"),
	priceSpan = document.querySelector("#price");

/**
 * Calculate cost of delivery and display it on the page
 */
function calcDeliveryCost() {
	let priceByVolume = (3 * parseInt(length.value) * parseInt(height.value) * parseInt(width.value)).toFixed(2);
	let priceByWeight = (5.6 * parseInt(weight.value)).toFixed(2);
	if (parseFloat(priceByVolume) > parseFloat(priceByWeight)) {
		priceSpan.innerText = priceByVolume;
		priceInput.value = parseInt(priceByVolume);
	} else {
		priceSpan.innerText = priceByWeight;
		priceInput.value = priceByWeight;
	}
}

calcForm.addEventListener("submit", (e) => {
	e.preventDefault();
	calcDeliveryCost();
	calcForm.submit();
});

/**
 * Sorting table columns asc/desc
 *
 * @param {HTMLTableElement} table - table to sort
 * @param {number} column - index of the table column to sort
 * @param {boolean} asc - determinate that sorting will be ascended
 */
function sortTableByColumn(table, column, asc = true) {
	const tableBody = table.tBodies[0];
	const tableRows = Array.from(tableBody.querySelectorAll("tr"));
	const directionMode = asc ? 1 : -1;

	const sortedTableRows = tableRows.sort((a, b) => {
		let aColText;
		let bColText;

		if (column !== 1) {
			aColText = a
				.querySelector(`td:nth-child(${column + 1})`)
				.textContent.trim();
			bColText = b
				.querySelector(`td:nth-child(${column + 1})`)
				.textContent.trim();
		} else {
			aColText = parseInt(
				a.querySelector(`td:nth-child(${column + 1})`).textContent.trim()
			);
			bColText = parseInt(
				b.querySelector(`td:nth-child(${column + 1})`).textContent.trim()
			);
		}
		return aColText > bColText ? 1 * directionMode : -1 * directionMode;
	});

	while (tableBody.firstChild) {
		tableBody.removeChild(tableBody.firstChild);
	}

	tableBody.append(...sortedTableRows);

	table.querySelectorAll("th")
		.forEach((th) => th.classList.remove("th-sort-asc", "th-sort-desc"));
	table.querySelector(`th:nth-child(${column + 1})`)
		.classList.toggle("th-sort-asc", asc);
	table.querySelector(`th:nth-child(${column + 1})`)
		.classList.toggle("th-sort-desc", !asc);
}

document.querySelectorAll(".table-sort th").forEach((headerCell) => {
	headerCell.addEventListener("click", () => {
		const tableElement = headerCell.parentElement.parentElement.parentElement;
		const headerIndex = Array.prototype.indexOf.call(
			headerCell.parentElement.children,
			headerCell
		);
		const isAsc = headerCell.classList.contains("th-sort-asc");

		sortTableByColumn(tableElement, headerIndex, !isAsc);
	});
});
