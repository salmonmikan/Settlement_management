document.addEventListener("DOMContentLoaded", function () {
  const tripTypeSelect = document.querySelector('select[name="tripType"]');
  const regionTypeSelect = document.querySelector('select[name="regionType"]');
  const rankTypeInput = document.querySelector('input[name="rankType"]');
  const dailyField = document.querySelector('input[name="dailyAllowance"]');
  const hotelField = document.querySelector('input[name="hotelFee"]');

  function fetchAllowance() {
    const tripType = tripTypeSelect.value;
    const regionType = regionTypeSelect.value;
    const rankType = rankTypeInput.value;

    if (tripType && regionType && rankType) {
      fetch("GetAllowanceServlet", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `tripType=${encodeURIComponent(tripType)}&regionType=${encodeURIComponent(regionType)}&rankType=${encodeURIComponent(rankType)}`
      })
      .then((res) => res.json())
      .then((data) => {
        if (data.daily) dailyField.value = data.daily;
        if (data.hotel !== undefined) hotelField.value = data.hotel;
      })
      .catch((err) => {
        console.error("取得失敗:", err);
      });
    }
  }

  tripTypeSelect.addEventListener("change", fetchAllowance);
  regionTypeSelect.addEventListener("change", fetchAllowance);
});