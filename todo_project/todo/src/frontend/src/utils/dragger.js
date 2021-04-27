// npm install dragula
import dragula from "dragula";
import "dragula/dist/dragula.css";

const dragger = {
  // 초기화 메소드
  init(container) {
    // container를 배열로 받아서 객체를 생성(전달)
    return dragular([...container]);
  },
  silblings({ el, wrapper, candidates, type }) {
    // candidates 배열을 돌 수 있는 후보군 , type : card or list
    //현재 아이디를 가져와서
    const curId = el.dataset[type + "Id"] * 1; // listId or cardId

    // const targetCard = { //어디로 이동해야할지 정보를 갖는 객체
    //     id : el.dataset.cardId * 1, //문자열을 숫자로 바꿔주는 작업
    //     pos : 65535
    // }

    let prev = null;
    let next = null;

    // 카드 리스트 배열 뽑기 - 호출하는 부분에서 전달해 줄 예정
    // Array.from(wrapper.querySelectorAll('.card-item'))
    candidates.forEach((el, idx, arr) => {
      //카드하나씩 뽑아와서 위치 체크하기
      const id = el.dataset[type + "Id"] * 1;
      if (id === curId) {
        // 카드 아이디가, 옮기려고 하는 카드id와 동일하다면
        // 앞뒤 카드 계산
        prevCard =
          idx > 0
            ? {
                id: arr[idx - 1].dataset[type + "Id"] * 1,
                pos: arr[idx - 1].dataset[type + "Pos"] * 1,
              }
            : null; // idx가 0보다 크지 않으면, 앞에 카드는 없는 걸로!
        nextCard =
          idx < arr.length - 1
            ? {
                //마지막 카드가 아니라면
                id: arr[idx + 1].dataset[type + "Id"] * 1,
                pos: arr[idx + 1].dataset[type + "Pos"] * 1,
              }
            : null;
      }
    });
    return { prev, next };
  },
};

export default dragger;
