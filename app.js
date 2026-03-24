// ===== 상태 관리 =====
const state = {
    level: 'elementary',
    setSize: 20,
    currentSet: 0,
    currentIndex: 0,
    words: [],           // 현재 세트의 단어들
    memorized: new Set(), // 이번 세션에서 외운 단어 인덱스
    isFlipped: false,
};

// ===== 로컬 스토리지 =====
function getStorageKey(level) {
    return `voca_memorized_${level}`;
}

function getMemorizedWords(level) {
    const data = localStorage.getItem(getStorageKey(level));
    return data ? new Set(JSON.parse(data)) : new Set();
}

function saveMemorizedWord(level, word) {
    const memorized = getMemorizedWords(level);
    memorized.add(word);
    localStorage.setItem(getStorageKey(level), JSON.stringify([...memorized]));
}

function resetProgress(level) {
    localStorage.removeItem(getStorageKey(level));
}

function resetAllProgress() {
    ['elementary', 'intermediate', 'advanced'].forEach(level => {
        localStorage.removeItem(getStorageKey(level));
    });
}

// ===== DOM 요소 =====
const screens = {
    start: document.getElementById('start-screen'),
    study: document.getElementById('study-screen'),
    complete: document.getElementById('complete-screen'),
};

function showScreen(name) {
    Object.values(screens).forEach(s => s.classList.remove('active'));
    screens[name].classList.add('active');
}

// ===== 시작 화면 로직 =====
function initStartScreen() {
    // 레벨 선택
    document.querySelectorAll('.level-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.level-btn').forEach(b => b.classList.remove('selected'));
            btn.classList.add('selected');
            state.level = btn.dataset.level;
            updateSetSelector();
            updateProgressSummary();
        });
    });

    // 세트 크기 선택
    document.querySelectorAll('.set-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.set-btn').forEach(b => b.classList.remove('selected'));
            btn.classList.add('selected');
            state.setSize = parseInt(btn.dataset.size);
            updateSetSelector();
        });
    });

    // 시작 버튼
    document.getElementById('start-btn').addEventListener('click', startStudy);

    // 초기화 버튼
    document.getElementById('reset-btn').addEventListener('click', () => {
        if (confirm('모든 단계의 학습 기록이 초기화됩니다. 계속하시겠습니까?')) {
            resetAllProgress();
            updateSetSelector();
            updateProgressSummary();
        }
    });

    updateSetSelector();
    updateProgressSummary();
}

function updateSetSelector() {
    const container = document.getElementById('set-selector');
    const totalWords = 900;
    const setCount = Math.ceil(totalWords / state.setSize);
    const memorized = getMemorizedWords(state.level);
    const words = WORD_DATA[state.level];

    container.innerHTML = '';

    for (let i = 0; i < setCount; i++) {
        const start = i * state.setSize;
        const end = Math.min(start + state.setSize, totalWords);
        const setWords = words.slice(start, end);

        // 이 세트에서 외운 단어 수 계산
        const memorizedInSet = setWords.filter(w => memorized.has(w.word)).length;
        const isCompleted = memorizedInSet === setWords.length;

        const item = document.createElement('div');
        item.className = `set-item${i === state.currentSet ? ' selected' : ''}${isCompleted ? ' completed' : ''}`;
        item.innerHTML = `
            세트 ${i + 1}
            ${isCompleted ? '<span class="set-check">✓</span>' : ''}
            <div style="font-size:0.7rem;color:var(--text-light);margin-top:2px;">${memorizedInSet}/${setWords.length}</div>
        `;
        item.addEventListener('click', () => {
            container.querySelectorAll('.set-item').forEach(s => s.classList.remove('selected'));
            item.classList.add('selected');
            state.currentSet = i;
        });
        container.appendChild(item);
    }
}

function updateProgressSummary() {
    const container = document.getElementById('progress-summary');
    const memorized = getMemorizedWords(state.level);
    const total = 900;
    const count = memorized.size;
    const percent = Math.round((count / total) * 100);

    const levelNames = {
        elementary: '초급 (Elementary)',
        intermediate: '중급 (Intermediate)',
        advanced: '고급 (Advanced)',
    };

    container.innerHTML = `
        <h3>${levelNames[state.level]} 학습 현황</h3>
        <div class="progress-bar-bg">
            <div class="progress-bar-fill" style="width: ${percent}%"></div>
        </div>
        <div class="progress-text">${count} / ${total} 단어 완료 (${percent}%)</div>
    `;
}

// ===== 학습 화면 로직 =====
function startStudy() {
    const allWords = WORD_DATA[state.level];
    const start = state.currentSet * state.setSize;
    const end = Math.min(start + state.setSize, 900);
    const memorized = getMemorizedWords(state.level);

    // 이미 외운 단어 제외
    state.words = allWords.slice(start, end).filter(w => !memorized.has(w.word));
    state.currentIndex = 0;
    state.memorized = new Set();
    state.isFlipped = false;

    if (state.words.length === 0) {
        alert('이 세트의 모든 단어를 이미 외웠습니다! 다른 세트를 선택하세요.');
        return;
    }

    document.getElementById('total-count').textContent = state.words.length;
    updateStudyUI();
    showScreen('study');
}

function updateStudyUI() {
    if (state.currentIndex >= state.words.length) {
        showCompleteScreen();
        return;
    }

    const word = state.words[state.currentIndex];
    const card = document.getElementById('word-card');
    const inner = card.querySelector('.card-inner');

    // 카드 리셋
    inner.classList.remove('flipped');
    state.isFlipped = false;
    card.style.transform = '';
    card.style.opacity = '';
    card.classList.remove('animate-out', 'drag-up', 'drag-left', 'drag-right');

    // 앞면
    document.getElementById('word-text').textContent = word.word;

    // 뒷면
    document.getElementById('word-text-back').textContent = word.word;
    document.getElementById('pos-badge').textContent = word.pos;
    document.getElementById('meaning-text').textContent = word.meaning;

    // 동음이의어
    const homophonesSection = document.getElementById('homophones-section');
    const homophonesList = document.getElementById('homophones-list');

    if (word.homophones && word.homophones.length > 0) {
        homophonesSection.classList.add('visible');
        homophonesList.innerHTML = word.homophones
            .map(h => `<span class="homophone-tag">${h}</span>`)
            .join('');
    } else {
        homophonesSection.classList.remove('visible');
        homophonesList.innerHTML = '';
    }

    // UI 업데이트
    document.getElementById('current-index').textContent = state.currentIndex + 1;
    document.getElementById('memorized-count').textContent = state.memorized.size;

    const progress = ((state.currentIndex + 1) / state.words.length) * 100;
    document.getElementById('progress-bar').style.width = progress + '%';
}

function flipCard() {
    const inner = document.querySelector('.card-inner');
    state.isFlipped = !state.isFlipped;
    inner.classList.toggle('flipped');
}

function nextWord() {
    if (state.currentIndex < state.words.length - 1) {
        state.currentIndex++;
        updateStudyUI();
    } else {
        showCompleteScreen();
    }
}

function prevWord() {
    if (state.currentIndex > 0) {
        state.currentIndex--;
        updateStudyUI();
    }
}

function memorizeWord() {
    const word = state.words[state.currentIndex];
    state.memorized.add(state.currentIndex);
    saveMemorizedWord(state.level, word.word);

    // 애니메이션
    const card = document.getElementById('word-card');
    card.classList.add('animate-out');
    card.style.transform = 'translateY(-300px) rotate(-10deg)';
    card.style.opacity = '0';

    setTimeout(() => {
        // 외운 단어를 목록에서 제거
        state.words.splice(state.currentIndex, 1);
        if (state.currentIndex >= state.words.length) {
            state.currentIndex = Math.max(0, state.words.length - 1);
        }
        document.getElementById('total-count').textContent = state.words.length;

        if (state.words.length === 0) {
            showCompleteScreen();
        } else {
            updateStudyUI();
        }
    }, 350);
}

function showCompleteScreen() {
    const memorized = getMemorizedWords(state.level);
    const totalMemorized = memorized.size;
    const remaining = 900 - totalMemorized;

    document.getElementById('stat-memorized').textContent = totalMemorized;
    document.getElementById('stat-remaining').textContent = remaining;
    document.getElementById('complete-message').textContent =
        remaining === 0
            ? '축하합니다! 모든 단어를 외웠습니다!'
            : `전체 900개 중 ${totalMemorized}개를 외웠습니다. 계속 화이팅!`;

    // 남은 단어 다시 학습 버튼
    const retryBtn = document.getElementById('retry-btn');
    if (state.words.length > 0) {
        retryBtn.style.display = 'block';
        retryBtn.textContent = `남은 ${state.words.length}개 단어 다시 학습`;
    } else {
        retryBtn.style.display = 'none';
    }

    showScreen('complete');
}

// ===== 드래그 / 스와이프 핸들링 =====
function initDragHandlers() {
    const card = document.getElementById('word-card');
    let startX = 0, startY = 0;
    let currentX = 0, currentY = 0;
    let isDragging = false;

    function onStart(x, y) {
        isDragging = true;
        startX = x;
        startY = y;
        currentX = x;
        currentY = y;
        card.classList.add('dragging');
        card.classList.remove('animate-out');
    }

    function onMove(x, y) {
        if (!isDragging) return;
        currentX = x;
        currentY = y;
        const dx = currentX - startX;
        const dy = currentY - startY;
        const rotation = dx * 0.1;

        card.style.transform = `translate(${dx}px, ${dy}px) rotate(${rotation}deg)`;

        // 방향 힌트
        card.classList.remove('drag-up', 'drag-left', 'drag-right');
        if (dy < -50) card.classList.add('drag-up');
        else if (dx < -50) card.classList.add('drag-left');
        else if (dx > 50) card.classList.add('drag-right');
    }

    function onEnd() {
        if (!isDragging) return;
        isDragging = false;
        card.classList.remove('dragging', 'drag-up', 'drag-left', 'drag-right');

        const dx = currentX - startX;
        const dy = currentY - startY;
        const threshold = 80;

        if (dy < -threshold && Math.abs(dy) > Math.abs(dx)) {
            // 위로 드래그 → 외웠어요
            memorizeWord();
        } else if (dx < -threshold && Math.abs(dx) > Math.abs(dy)) {
            // 왼쪽으로 드래그 → 다음 단어
            card.classList.add('animate-out');
            card.style.transform = `translateX(-400px) rotate(-20deg)`;
            card.style.opacity = '0';
            setTimeout(() => nextWord(), 300);
        } else if (dx > threshold && Math.abs(dx) > Math.abs(dy)) {
            // 오른쪽으로 드래그 → 이전 단어
            card.classList.add('animate-out');
            card.style.transform = `translateX(400px) rotate(20deg)`;
            card.style.opacity = '0';
            setTimeout(() => prevWord(), 300);
        } else {
            // 원래 위치로
            card.style.transition = 'transform 0.3s ease, opacity 0.3s ease';
            card.style.transform = '';
            card.style.opacity = '';
            setTimeout(() => {
                card.style.transition = '';
            }, 300);
        }
    }

    // 마우스 이벤트
    card.addEventListener('mousedown', (e) => {
        e.preventDefault();
        onStart(e.clientX, e.clientY);
    });

    document.addEventListener('mousemove', (e) => {
        onMove(e.clientX, e.clientY);
    });

    document.addEventListener('mouseup', () => {
        onEnd();
    });

    // 터치 이벤트
    card.addEventListener('touchstart', (e) => {
        const touch = e.touches[0];
        onStart(touch.clientX, touch.clientY);
    }, { passive: true });

    document.addEventListener('touchmove', (e) => {
        const touch = e.touches[0];
        onMove(touch.clientX, touch.clientY);
    }, { passive: true });

    document.addEventListener('touchend', () => {
        onEnd();
    });

    // 클릭으로 카드 뒤집기 (드래그가 아닌 경우)
    card.addEventListener('click', (e) => {
        const dx = Math.abs(currentX - startX);
        const dy = Math.abs(currentY - startY);
        if (dx < 10 && dy < 10) {
            flipCard();
        }
    });
}

// ===== 키보드 단축키 =====
function initKeyboardHandlers() {
    document.addEventListener('keydown', (e) => {
        if (!screens.study.classList.contains('active')) return;

        switch (e.key) {
            case 'ArrowLeft':
                nextWord();
                break;
            case 'ArrowRight':
                prevWord();
                break;
            case 'ArrowUp':
                e.preventDefault();
                memorizeWord();
                break;
            case ' ':
                e.preventDefault();
                flipCard();
                break;
        }
    });
}

// ===== 네비게이션 =====
function initNavigation() {
    document.getElementById('back-btn').addEventListener('click', () => {
        updateSetSelector();
        updateProgressSummary();
        showScreen('start');
    });

    document.getElementById('home-btn').addEventListener('click', () => {
        updateSetSelector();
        updateProgressSummary();
        showScreen('start');
    });

    document.getElementById('retry-btn').addEventListener('click', () => {
        state.currentIndex = 0;
        state.isFlipped = false;
        document.getElementById('total-count').textContent = state.words.length;
        updateStudyUI();
        showScreen('study');
    });
}

// ===== 초기화 =====
function init() {
    initStartScreen();
    initDragHandlers();
    initKeyboardHandlers();
    initNavigation();
}

document.addEventListener('DOMContentLoaded', init);
