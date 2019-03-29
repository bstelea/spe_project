var simplemaps_continentmap_mapdata={
    main_settings: {
        //General settings
        width: 'responsive', //or 'responsive'
        background_color: "#FFFFFF",
        background_transparent: "yes",
        border_color: "#000",
        popups: "detect",

        //State defaults
        state_description: "",//"State description",
        // state_color: "#88A4BC",
        // state_hover_color: "#3B729F",
        state_color: "#75bfca",
        state_hover_color: "#66a6af",
        state_url: "https://globalbeershop.spe.cs.bris.ac.uk/",
        border_size: 1.5,
        all_states_inactive: "no",
        all_states_zoomable: "no",
        // color: "#3366FF",
        // hover_color: "#1c388c",

        //Location defaults
        location_description: "Location description",
        location_color: "#FF0067",
        location_opacity: 0.8,
        location_hover_opacity: 1,
        location_url: "",
        location_size: 25,
        location_type: "square",
        location_image_source: "frog.png",
        location_border_color: "#FFFFFF",
        location_border: 2,
        location_hover_border: 2.5,
        all_locations_inactive: "no",
        all_locations_hidden: "no",

        //Label defaults
        label_color: "#d5ddec",
        label_hover_color: "#d5ddec",
        label_size: 22,
        label_font: "Arial",
        hide_labels: "no",

        //Zoom settings
        manual_zoom: "no",
        back_image: "no",
        arrow_color: "#cecece",
        arrow_color_border: "#808080",
        initial_back: "no",
        initial_zoom: -1,
        initial_zoom_solo: "no",
        region_opacity: 1,
        region_hover_opacity: 0.6,
        zoom_out_incrementally: "yes",
        zoom_percentage: 0.99,
        zoom_time: 0.5,

        //Popup settings
        popup_color: "white",
        popup_opacity: 0.9,
        popup_shadow: 1,
        popup_corners: 5,
        popup_font: "12px/1.5 Verdana, Arial, Helvetica, sans-serif",
        popup_nocss: "no",

        //Advanced settings
        div: "intmap",
        auto_load: "yes",
        url_new_tab: "no",
        images_directory: "default",
        fade_time: 0.1,
        link_text: "(View Link)"
    },
    state_specific: {
        SA: {
            name: "South America",
            description: "default",
            // color: "#CC33FF",
            // hover_color: "#751d92",
            color: "default",
            hover_color: "default",
            url: "https://globalbeershop.spe.cs.bris.ac.uk/shop?continent=South%20America"
        },
        NA: {
            name: "North America",
            description: "default",
            // color: "#3366FF",
            // hover_color: "#1c388c",
            color: "default",
            hover_color: "default",
            url: "https://globalbeershop.spe.cs.bris.ac.uk/shop?continent=North%20America"
        },
        EU: {
            name: "Europe",
            description: "default",
            // color: "#FF3366",
            // hover_color: "#c0264d",
            color: "default",
            hover_color: "default",
            url: "https://globalbeershop.spe.cs.bris.ac.uk/shop?continent=Europe"
        },
        AF: {
            name: "Africa",
            description: "default",
            // color: "#33FF66",
            // hover_color: "#1a8535",
            color: "default",
            hover_color: "default",
            url: "https://globalbeershop.spe.cs.bris.ac.uk/shop?continent=Africa"
        },
        NS: {
            name: "North Asia",
            description: "default",
            // color: "#33FFCC",
            // hover_color: "#23b28e",
            color: "default",
            hover_color: "default",
            url: "https://globalbeershop.spe.cs.bris.ac.uk/shop?continent=North%20Asia"
        },
        SS: {
            name: "South Asia",
            description: "default",
            // color: "#FF6633",
            // hover_color: "#ac4422",
            color: "default",
            hover_color: "default",
            url: "https://globalbeershop.spe.cs.bris.ac.uk/shop?continent=South%20Asia"
        },
        ME: {
            name: "Middle East",
            description: "default",
            // color: "#FFCC33",
            // hover_color: "#bb9525",
            color: "default",
            hover_color: "default",
            url: "https://globalbeershop.spe.cs.bris.ac.uk/shop?continent=Middle%20East"
        },
        OC: {
            name: "Oceania",
            description: "default",
            // color: "#FF33CC",
            // hover_color: "#b1238d",
            color: "default",
            hover_color: "default",
            url: "https://globalbeershop.spe.cs.bris.ac.uk/shop?continent=Oceania"
        }
    },
    // locations: {
    //   "0": {
    //     name: "New York",
    //     lat: 40.71,
    //     lng: -74.0059731,
    //     description: "default",
    //     color: "default",
    //     url: "default",
    //     size: "default"
    //   },
    //   "1": {
    //     name: "London",
    //     lat: 51.5073346,
    //     lng: -0.1276831,
    //     description: "default",
    //     color: "default",
    //     url: "default"
    //   }
    // },
    labels: {}
};