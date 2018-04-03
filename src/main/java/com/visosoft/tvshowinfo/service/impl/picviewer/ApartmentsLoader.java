package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.google.common.collect.ImmutableList;
import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApartmentsLoader implements PicLoader {

    private static final Logger logger = LoggerFactory.getLogger(ApartmentsLoader.class);
    private final PicViewerDao picViewerDao;

    public ApartmentsLoader(PicViewerDao picViewerDao) {
        this.picViewerDao = picViewerDao;
    }

    @Override
    public List<PicViewerRecord> loadPics() {
        List<PicViewerRecord> toAdd = new ArrayList<>();
        try {
            toAdd.addAll(processOtoDom());
            toAdd.addAll(gumtree());
        } catch (Exception e) {
            logger.error("Exception in apartments loader", e);
        }
        return toAdd;
    }

    private Collection<? extends PicViewerRecord> gumtree() throws IOException {
        List<PicViewerRecord> toAdd = new ArrayList<>();
        String pageContents = contents("https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/krakow/mieszkanie/v1c9073l3200208a1dwp1?nr=2&pr=300000,490000");
        int pos;
        int endPos = 0;
        Pattern detailsPagePattern = Pattern.compile("<a class=\"href-link\" href=\"([^\"]+)");
        Pattern detailsPageTitlePattern = Pattern.compile("<span class=\"myAdTitle\">([^<]+)");
        Pattern detailsPageDescPattern = Pattern.compile("<div class=\"description\"(.*?)</div", Pattern.MULTILINE | Pattern.DOTALL);
        String detailsPageSizeKey = "<span class=\"name\">Wielkość (m2)</span>";
        Pattern detailsPageSizePattern = Pattern.compile("<span class=\"value\">([^<]+)");
        while ((pos = pageContents.indexOf("<li class=\"result pictures\"", endPos)) != -1) {
            endPos = pageContents.indexOf("</li", pos);
            String detailsUrl = "https://www.gumtree.pl" + fromPattern(detailsPagePattern, pageContents.substring(pos, endPos));
            if (isNew(detailsUrl)) {
                String detailsContents = contents(detailsUrl);
                if (detailsContents.contains(detailsPageSizeKey)) {
                    int size = Integer.parseInt(fromPattern(detailsPageSizePattern, detailsContents.substring(detailsContents.indexOf(detailsPageSizeKey))));
                    if (size < 46 || size > 60)
                        continue;
                }
                String title = fromPattern(detailsPageTitlePattern, detailsContents);
                String desc = fromPattern(detailsPageDescPattern, detailsContents);
                if (looksInteresting(title + " " + desc)) {
                    PicViewerRecord record = new PicViewerRecord();
                    record.setGroupName("apartments");
                    record.setTitle(title);
                    record.setUrl(detailsUrl);
                    toAdd.add(record);
                }
            }
        }
        logger.info("Added gumtree {}", toAdd.size());
        return toAdd;
    }

    private Collection<? extends PicViewerRecord> processOtoDom() throws IOException {
        List<PicViewerRecord> toAdd = new ArrayList<>();
        String pageContents = contents("https://www.otodom.pl/sprzedaz/mieszkanie/krakow/?search%5Bfilter_float_price%3Afrom%5D=300000&search%5Bfilter_float_price%3Ato%5D=500000&search%5Bfilter_float_price_per_m%3Ato%5D=9000&search%5Bfilter_float_m%3Afrom%5D=43&search%5Bfilter_float_m%3Ato%5D=60&search%5Bfilter_enum_market%5D%5B0%5D=secondary&search%5Bfilter_enum_floor_no%5D%5B0%5D=floor_2&search%5Bfilter_enum_floor_no%5D%5B1%5D=floor_3&search%5Bfilter_enum_floor_no%5D%5B2%5D=floor_4&search%5Bfilter_enum_floor_no%5D%5B3%5D=floor_5&search%5Bfilter_enum_floor_no%5D%5B4%5D=floor_6&search%5Bfilter_enum_floor_no%5D%5B5%5D=floor_7&search%5Bfilter_float_building_floors_num%3Ato%5D=7&search%5Bfilter_float_build_year%3Afrom%5D=2004&search%5Bfilter_float_build_year%3Ato%5D=2016&search%5Bdescription%5D=1&search%5Border%5D=created_at_first%3Adesc&search%5Bdist%5D=0&search%5Bsubregion_id%5D=410&search%5Bcity_id%5D=38");
        pageContents = pageContents.substring(pageContents.indexOf("<div class=\"listing-title\">Wszystkie "));
        int pos;
        int endPos = 0;
        Pattern detailsPagePattern = Pattern.compile("data-url=\"([^\"]+)");
        Pattern detailsPageTitle1Pattern = Pattern.compile("<h1 itemprop=\"name\">([^<]+)");
        Pattern detailsPageTitle2Pattern = Pattern.compile("itemprop=\"address\">([^<]+)");
        Pattern detailsPageDescPattern = Pattern.compile("<div itemprop=\"description\">(.*?)</div", Pattern.MULTILINE | Pattern.DOTALL);
        while ((pos = pageContents.indexOf("<article", endPos)) != -1) {
            endPos = pageContents.indexOf("</article", pos);
            String detailsUrl = fromPattern(detailsPagePattern, pageContents.substring(pos, endPos));
            if (isNew(detailsUrl)) {
                String detailsContents = contents(detailsUrl);
                String title1 = fromPattern(detailsPageTitle1Pattern, detailsContents);
                String title2 = fromPattern(detailsPageTitle2Pattern, detailsContents);
                String desc = fromPattern(detailsPageDescPattern, detailsContents);
                if (looksInteresting(title1 + " " + title2 + " " + desc)) {
                    PicViewerRecord record = new PicViewerRecord();
                    record.setGroupName("apartments");
                    record.setTitle(title1 + " " + title2);
                    record.setUrl(detailsUrl);
                    toAdd.add(record);
                }
            }
        }
        logger.info("Added otodom {}", toAdd.size());
        return toAdd;
    }

    private final List<String> keyWordsToFilterOut = ImmutableList.of("mistrzejowice", "czyżyny", "nowa huta", "obozowa",
            "salwator", "płaszów", "2 poziomowe", "antresol", "termin realizacji", "parter", "pierwsze piętro", "pierwszym piętrze",
            "stan deweloperski");

    private boolean looksInteresting(String desc) {
        desc = desc.toLowerCase();
        for (String keyword : keyWordsToFilterOut) {
            if (desc.contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    String fromPattern(Pattern pattern, String string) {
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return matcher.group(1);
    }

    private boolean isNew(String detailsUrl) {
        return !picViewerDao.withUrlEndingExists(detailsUrl);
    }

    private static String contents(String url) throws IOException {
        return Request.Get(url).connectTimeout(5000).socketTimeout(10000).execute().returnContent().asString();
    }
}
