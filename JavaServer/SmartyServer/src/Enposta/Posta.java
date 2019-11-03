package Enposta;

import java.util.ArrayList;
import java.util.List;

import kr.co.shineware.nlp.posta.en.core.EnPosta;

public class Posta {

	/*
	 * getPosta로 형태소가 분리된 List return한다.
	 * 위의 List를 매개변수로 한 getPosList를 이용해 필요한 형태소의 동사만을 가져온다.
	 * 위의 List를 매개변수로 한 webCrolling을 거쳐 최종적으로 ArrayList<Word>의 객체로 가공한다.
	 */
	
	public ArrayList<Word> getFinalPosta(String posts) {
		List<String> list = getPosta(posts);
		ArrayList<String> PosList = getPosList(list);
		ArrayList<Word> WordList = webCrolling(PosList);
		
		return WordList;
	}
	
	/*
	 * webCrollong은 ArrayList 객체를 받아 웹으로 정보를 긁어온다.
	 * 항상 웹검색을 할 시 속도가 느려져 검색할때마다 파일로 저장한다.
	 * 저장되어있는 단어 정보를 가져오기도 한다.
	 * 최종적으로 ArrayList<Word>의 객체로 return 한다.
	 */
	public ArrayList<Word> webCrolling(ArrayList<String> list) {
		ArrayList<Word> array = new ArrayList<Word>();
		
		Crolling crolling = new Crolling();
		int length = list.size();
		for(int i = 0; i < length; i++ ) {
			WordFile wordFile = new WordFile();
			String wordName = list.get(i);
			Word word = null;
			
			if(wordFile.findWord(wordName)) {
				word = wordFile.getWord(wordName);
				continue;
			}
			
			word = crolling.getText(wordName);
			wordFile.setWord(word);
		}
		return array;
	}
	/*
	 * getPosList는 형태소가 분리된 List 객체를 받아 우리에게 필요한
	 * 명사, 동사, 대명사, 약간의 부사등으로 걸러준다.
	 * 최종적으로 걸러진 형태소만을 return 한다.
	 */
	
	private String[] posArray = {"JJ", "NN", "NNP", "NNPS", "NNS", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
	
	public ArrayList<String> getPosList(List<String> list) {
		ArrayList<String> posList = new ArrayList<String>();
		
		int length = list.size();
		
		for(int i = 0; i < length; i++) {
			String str = list.get(i);
			String[] str_ = str.split("/");
			String pos = str_[1];
			String spelling = str_[0];
			
			for(int l = 0; l < posList.size(); l++) {
				if(pos.equals(posArray[l]) ) {
					posList.add(spelling);
				}
			}
			
		}
		
		return posList;
	}
	
	/*
	 * getPosta는 EnPosta라는 라이브러리를 사용하여 긴 영어의 글을
	 * 형태소 분석한다. 형태소가 분석된 것을 List로 담아내어 return 한다.
	 */
	
	public List<String> getPosta(String posts) {
		EnPosta posta = new EnPosta();

		posta.load("src/model");

		// 사용자 사전 추가
		posta.appendUserDic("dic.user");
		posta.buildFailLink();

		List<String> resultList = posta.analyze(posts);
		
		return resultList;
		
	}

}
