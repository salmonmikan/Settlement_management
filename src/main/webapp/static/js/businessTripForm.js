document.addEventListener("DOMContentLoaded", function () {
  const tripTypeSelect = document.querySelector('select[name="tripType"]');
  const regionTypeSelect = document.querySelector('select[name="regionType"]');
  const rankTypeInput = document.querySelector('input[name="rankType"]');
  const dailyField = document.querySelector('input[name="dailyAllowance"]');
  const hotelField = document.querySelector('input[name="hotelFee"]');

  // contextPath được set từ JSP
  const fullPath = `${contextPath}/GetAllowanceServlet`;

  function fetchAllowance() {
    const tripType = tripTypeSelect.value;
    const regionType = regionTypeSelect.value;

    if (tripType && regionType) {
      fetch(fullPath, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `tripType=${encodeURIComponent(tripType)}&regionType=${encodeURIComponent(regionType)}`
      })
      .then((res) => res.json())
      .then((data) => {
        dailyField.value = typeof data.daily !== 'undefined' ? data.daily : '';
        hotelField.value = typeof data.hotel !== 'undefined' ? data.hotel : '';
      })
      .catch((err) => {
        console.error("取得失敗:", err);
      });
    }
  }

  tripTypeSelect.addEventListener("change", fetchAllowance);
  regionTypeSelect.addEventListener("change", fetchAllowance);
});