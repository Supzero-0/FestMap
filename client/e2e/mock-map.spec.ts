import test, { expect, Route } from "@playwright/test";

const mockFestivalsData = [
    { id: 1, name: 'Festival 1', city: 'City 1', genre: 'Rock', latitude: 45.75, longitude: 4.85, "startDate":"2024-06-28T10:00:00.000Z", "endDate":"2024-06-30T23:00:00.000Z" },
    { id: 2, name: 'Festival 2', city: 'City 2', genre: 'Pop', latitude: 45.76, longitude: 4.86, "startDate":"2024-07-05T10:00:00.000Z", "endDate":"2024-07-07T23:00:00.000Z" },
    { id: 3, name: 'Festival 3', city: 'City 3', genre: 'Electro', latitude: 45.77, longitude: 4.87, "startDate":"2024-08-15T10:00:00.000Z", "endDate":"2024-08-18T23:00:00.000Z" },
];

let festivals = [...mockFestivalsData];

test.describe('Mock festival map', () => {
    test.beforeEach(async ({ page }) => {
        festivals = [...mockFestivalsData];
        await page.route('**/api/festivals', (route: Route) => {
            if (route.request().method() === 'GET') {
                route.fulfill({
                    status: 200,
                    contentType: 'application/json',
                    body: JSON.stringify(festivals),
                });
            }
        });

        await page.goto('/');
        await expect(page.locator('app-festival-list .loading')).not.toBeVisible();
    });

    test('Carte visible', async ({ page }) => {
        await expect(page.getByTestId('festival-map')).toBeVisible();
    });

    test('Click sur item liste -> popup visible', async ({ page }) => {
        await page.locator('[data-testid^="festival-list-item-"]').first().click();
        await expect(page.getByTestId('festival-popup-1')).toBeVisible();
    });

    test('Marqueurs & nombre d"items === items.json', async ({ page }) => {
        const items = page.locator('[data-testid^="festival-list-item-"]');
        const markers = page.locator('[data-testid^="festival-marker-"]');

        await expect(items).toHaveCount(mockFestivalsData.length);
        await expect(markers).toHaveCount(mockFestivalsData.length);
    });

    test(`Action secondaire (delete) modifie l'UI`, async ({ page }) => {
        const items = page.locator('[data-testid^="festival-list-item-"]');
        const markers = page.locator('[data-testid^="festival-marker-"]');
        const initialItemCount = await items.count();
        const initialMarkersCount = await markers.count();

        await page.route('**/api/festivals/1', (route: Route) => {
            expect(route.request().method()).toBe('DELETE');
            festivals.shift();
            route.fulfill({ status: 204 });
        });

        // Action
        await page.getByTestId(`delete-festival-list-item-1`).click();

        // Assertions
        await expect(items).toHaveCount(initialItemCount - 1);
        await expect(markers).toHaveCount(initialMarkersCount - 1);
    });
});